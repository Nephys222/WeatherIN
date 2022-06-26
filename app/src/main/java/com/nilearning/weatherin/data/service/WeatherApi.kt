package com.nilearning.weatherin.data.service

import com.nilearning.weatherin.data.model.WeatherResponse
import com.nilearning.weatherin.util.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

// https://api.openweathermap.org/data/2.5/weather?q={city name}&appid=bd0ba391458fdf524a647fa02456f72b

interface WeatherApi {

    @GET("weather")
    suspend fun findCityWeatherData(
        @Query("q") q: String,
        @Query("units") units: String = "metric",
        @Query("appid") appid: String = Constants.API_KEY
    ): Response<WeatherResponse>

    companion object {
        operator fun invoke(
            networkConnectionInterceptor: ConnectionInterceptor
        ): WeatherApi {

            val serverURL = "http://api.openweathermap.org/data/2.5/"
            val okkHttpclient = OkHttpClient.Builder()
                .addInterceptor(networkConnectionInterceptor)
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .connectTimeout(1, TimeUnit.MINUTES)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .client(okkHttpclient)
                .baseUrl(serverURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApi::class.java)
        }
    }
}