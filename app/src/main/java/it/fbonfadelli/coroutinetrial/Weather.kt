package it.fbonfadelli.coroutinetrial

import java.io.Serializable

data class Weather(
    val id: Long,
    val weather_state_name: String,
    val wind_direction_compass: String,
    val applicable_date: String,
    val min_temp: Float,
    val max_temp: Float,
    val the_temp: Float,
    val wind_speed: Float,
    val wind_direction: Float,
    val air_pressure: Float,
    val humidity: Float,
    val visibility: Float,
    val predictability: Int
): Serializable
