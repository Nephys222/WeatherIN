package com.nilearning.weatherin.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Data class for Database entity and Serialization.
 */
@Entity(tableName = "WEATHER_DATA")
data class WeatherData(

    @PrimaryKey
    var id: Int? = 0,
    var temp: Double? = null,
    var icon: String? = null,
    var cityName: String? = null,
    var countryName: String? = null,
    var dateTime: String? = null,
    var humidity: Int? = null,
    var windSpeed: Double? = null,
    var visibility: Int? = null,
    var sunrise: Int? = null,
    var sunset: Int? = null
)