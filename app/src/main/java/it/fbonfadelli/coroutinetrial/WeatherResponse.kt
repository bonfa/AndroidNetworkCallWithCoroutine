package it.fbonfadelli.coroutinetrial

import java.io.Serializable

data class WeatherResponse(
    val consolidated_weather: List<Weather>
) : Serializable