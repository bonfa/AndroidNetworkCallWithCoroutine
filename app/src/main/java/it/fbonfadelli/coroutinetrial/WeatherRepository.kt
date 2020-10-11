package it.fbonfadelli.coroutinetrial

class WeatherRepository(private val weatherService: WeatherService) {
    suspend fun getWeatherFor(locationId: String): WeatherResponse {
        return weatherService.get(locationId)
    }
}

