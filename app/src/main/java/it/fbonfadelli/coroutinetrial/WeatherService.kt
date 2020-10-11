package it.fbonfadelli.coroutinetrial

import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherService {

    @GET("/api/location/{woeid}/")
    suspend fun get(@Path("woeid") id: String): WeatherResponse
}
