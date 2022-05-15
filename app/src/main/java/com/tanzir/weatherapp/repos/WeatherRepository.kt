package com.tanzir.weatherapp.repos

import android.location.Location
import com.tanzir.weatherapp.file.NetworkService
import com.tanzir.weatherapp.file.weather_api_key
import com.tanzir.weatherapp.models.CurrentModel
import com.tanzir.weatherapp.models.ForecastModel

class WeatherRepository {

    suspend fun fetchCurrentData(location: Location): CurrentModel {
        val endUrl =
            "weather?lat=${location.latitude}&lon=${location.longitude}&appid=$weather_api_key"
        return NetworkService.weatherServiceApi
            .getCurrentWeather(endUrl)


    }
    suspend fun fetchForecastData(location: Location): ForecastModel {
        val endUrl =
            "forecast?lat=${location.latitude}&lon=${location.longitude}&appid=$weather_api_key"
        return NetworkService.weatherServiceApi
            .getForecastWeather(endUrl)


    }
}


