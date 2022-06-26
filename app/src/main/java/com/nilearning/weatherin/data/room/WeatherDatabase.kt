package com.nilearning.weatherin.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.nilearning.weatherin.data.model.WeatherData

@Database(
    entities = [WeatherData::class],
    version = 1
)
abstract class WeatherDatabase : RoomDatabase() {

    abstract fun getWeatherDao(): WeatherDataDao

    companion object {

        @Volatile
        private var INSTANCE: WeatherDatabase? = null

        operator fun invoke(context: Context) = INSTANCE ?: synchronized(this) {
            INSTANCE ?: buildDatabase(context).also {
                INSTANCE = it
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                WeatherDatabase::class.java,
                "WEATHER_DATABASE"
            ).build()
    }
}
