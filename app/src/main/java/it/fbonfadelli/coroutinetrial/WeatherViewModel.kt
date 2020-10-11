package it.fbonfadelli.coroutinetrial

import ViewModelFactory
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class WeatherViewModel : ViewModel() {

    companion object {
        val INSTANCE = ViewModelFactory().create(WeatherViewModel::class.java)
    }

    private val errorMessage = MutableLiveData<ViewMessage>()
    private val errorMessageVisible = MutableLiveData<Boolean>()
    private val weather = MutableLiveData<ViewWeather>()
    private val weatherVisible = MutableLiveData<Boolean>()
    private val loaderVisible = MutableLiveData<Boolean>()

    fun getErrorMessage(): LiveData<ViewMessage> = errorMessage
    fun isErrorMessageVisible(): LiveData<Boolean> = errorMessageVisible //todo does it make sense to separate the visibility from the content?
    fun getWeather(): LiveData<ViewWeather> = weather
    fun isWeatherVisible(): LiveData<Boolean> = weatherVisible
    fun isLoaderVisible(): LiveData<Boolean> = loaderVisible
}

data class ViewMessage(val content: String)

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
