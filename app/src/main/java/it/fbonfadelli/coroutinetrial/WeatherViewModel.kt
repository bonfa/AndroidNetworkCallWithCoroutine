package it.fbonfadelli.coroutinetrial

import ViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.lang.Exception

private const val locationId = "44418"

class WeatherViewModel : ViewModel() {
    private val repository: WeatherRepository = WeatherRepository(WeatherServiceFactory.make())

    private val errorMessage = MutableLiveData<String>()
    private val errorMessageVisible = MutableLiveData<Boolean>()
    private val weather = MutableLiveData<ViewWeather>()
    private val weatherVisible = MutableLiveData<Boolean>()
    private val loaderVisible = MutableLiveData<Boolean>()

    fun getErrorMessage(): LiveData<String> = errorMessage
    fun isErrorMessageVisible(): LiveData<Boolean> = errorMessageVisible //todo does it make sense to separate the visibility from the content?

    fun getWeather(): LiveData<ViewWeather> = weather
    fun isWeatherVisible(): LiveData<Boolean> = weatherVisible
    fun isLoaderVisible(): LiveData<Boolean> = loaderVisible

    fun updateWeather() {
        viewModelScope.launch {
            try {
                loaderVisible.value = true
                val weatherResponse = repository.getWeatherFor(locationId)
                if (weatherResponse.consolidated_weather.isEmpty()) {
                    errorMessageVisible.value = true
                    errorMessage.value = "no location found"
                    weatherVisible.value = false
                } else {
                    errorMessageVisible.value = false
                    errorMessage.value = ""
                    weather.value = toViewModel(weatherResponse.consolidated_weather.first())
                    weatherVisible.value = true
                }
            } catch (e: Exception) {
                errorMessageVisible.value = true
                errorMessage.value = "network error"
                weatherVisible.value = false
            } finally {
                loaderVisible.value = false
            }
        }
    }

    private fun toViewModel(weather: Weather) =
        ViewWeather(
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
        errorMessage.value = "view reset"
        errorMessageVisible.value = true
        weatherVisible.value = false
        loaderVisible.value = false
    }

    companion object {
        val INSTANCE = ViewModelFactory().create(WeatherViewModel::class.java)
    }
}

data class ViewWeather(
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
