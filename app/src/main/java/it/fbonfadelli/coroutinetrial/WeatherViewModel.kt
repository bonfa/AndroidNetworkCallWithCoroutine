package it.fbonfadelli.coroutinetrial

import ViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

private const val locationId = "44418"

class WeatherViewModel : ViewModel() {
    private val repository: WeatherRepository = WeatherRepository(WeatherServiceFactory.make())

    private val message = MutableLiveData<Visible<String>>()
    private val weather = MutableLiveData<Visible<ViewWeatherContent>>()
    private val loaderVisible = MutableLiveData<Boolean>()

    fun message(): LiveData<Visible<String>> = message
    fun weather(): LiveData<Visible<ViewWeatherContent>> = weather
    fun isLoaderVisible(): LiveData<Boolean> = loaderVisible

    fun updateWeather() {
        viewModelScope.launch { getViewModel() }
    }

    private suspend fun getViewModel() {
        try {
            loaderVisible.value = true
            val weatherResponse = repository.getWeatherFor(locationId)
            if (weatherResponse.consolidated_weather.isEmpty()) {
                message.value = Visible.visibleWithContent("no location found")
                weather.value = Visible.hidden()
            } else {
                message.value = Visible.hidden()
                weather.value = Visible.visibleWithContent(toViewModel(weatherResponse.consolidated_weather.first()))
            }
        } catch (e: Exception) {
            message.value = Visible.visibleWithContent("network error")
            weather.value = Visible.hidden()
        } finally {
            loaderVisible.value = false
        }
    }

    private fun toViewModel(weather: Weather) =
        ViewWeatherContent(
            weatherStateName = weather.weather_state_name,
            windDirectionCompass = weather.wind_direction_compass,
            applicableDate = weather.applicable_date,
            minTemperature = "%.2f".format(weather.min_temp),
            maxTemperature = "%.2f".format(weather.max_temp),
            currentTemperature = "%.2f".format(weather.the_temp),
            windSpeed = "%.2f".format(weather.wind_speed),
            windDirection = "%.2f".format(weather.wind_direction),
            airPressure = "%.2f".format(weather.air_pressure),
            humidity = "%.2f".format(weather.humidity)
        )

    fun resetView() {
        loaderVisible.value = true
        message.value = Visible.visibleWithContent("view reset")
        weather.value = Visible.hidden()
        loaderVisible.value = false
    }

    companion object {
        val INSTANCE = ViewModelFactory().create(WeatherViewModel::class.java)
    }
}

class Visible<T>private constructor(
    val visible: Boolean,
    val content: T?
){
    companion object {
        fun <T> visibleWithContent(content: T): Visible<T> = Visible(true, content)
        fun <T> hidden(): Visible<T> = Visible(false, null)
    }
}

data class ViewWeatherContent(
    val weatherStateName: String,
    val windDirectionCompass: String,
    val applicableDate: String,
    val minTemperature: String,
    val maxTemperature: String,
    val currentTemperature: String,
    val windSpeed: String,
    val windDirection: String,
    val airPressure: String,
    val humidity: String
)
