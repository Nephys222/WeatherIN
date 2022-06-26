package com.nilearning.weatherin.data.repository

import com.nilearning.weatherin.data.model.WeatherData
import com.nilearning.weatherin.data.model.WeatherResponse
import com.nilearning.weatherin.data.room.WeatherDatabase
import com.nilearning.weatherin.data.service.ApiRequest
import com.nilearning.weatherin.data.service.WeatherApi

class WeatherRepository (
    private val api: WeatherApi,
    private val database: WeatherDatabase
    ) : ApiRequest() {

    suspend fun findCityWeather(cityName: String): WeatherResponse = apiRequest {
        api.findCityWeatherData(cityName)
    }

    suspend fun addWeather(weatherData: WeatherData) {
        database.getWeatherDao().addWeather(weatherData)
    }

    suspend fun fetchWeatherData(cityName: String): WeatherData? =
        database.getWeatherDao().fetchWeatherByCity(cityName)
    }