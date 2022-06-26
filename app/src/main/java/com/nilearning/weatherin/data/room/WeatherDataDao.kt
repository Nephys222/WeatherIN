package com.nilearning.weatherin.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.nilearning.weatherin.data.model.WeatherData

@Dao
interface WeatherDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addWeather(weatherData: WeatherData)

    @Query("SELECT * FROM WEATHER_DATA WHERE cityName = :cityName")
    suspend fun fetchWeatherByCity(cityName: String): WeatherData?
}
