package com.nilearning.weatherin.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nilearning.weatherin.data.model.WeatherData
import com.nilearning.weatherin.data.model.WeatherResponse
import com.nilearning.weatherin.data.repository.WeatherRepository
import com.nilearning.weatherin.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class WeatherViewModel(private val repository: WeatherRepository) : ViewModel() {

    private val _weatherLiveData =
        MutableLiveData<Event<State<WeatherData>>>()
    val weatherLiveData: LiveData<Event<State<WeatherData>>>
        get() = _weatherLiveData

    private lateinit var weatherResponse: WeatherResponse

    private fun findCityWeather(cityName: String) {
        _weatherLiveData.postValue(Event(State.loading()))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                weatherResponse =
                    repository.findCityWeather(cityName)
                addWeatherDataIntoDb(weatherResponse)
                withContext(Dispatchers.Main) {
                    val weatherData = WeatherData()
                    weatherData.icon = weatherResponse.weather.first().icon
                    weatherData.cityName = weatherResponse.name
                    weatherData.countryName = weatherResponse.sys.country
                    weatherData.temp = weatherResponse.main.temp
                    weatherData.humidity = weatherResponse.main.humidity
                    weatherData.windSpeed = weatherResponse.wind.speed
                    weatherData.visibility = weatherResponse.visibility
                    weatherData.sunrise = weatherResponse.sys.sunrise
                    weatherData.sunset = weatherResponse.sys.sunset
                    _weatherLiveData.postValue(
                        Event(
                            State.success(
                                weatherData
                            )
                        )
                    )
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(Event(State.error(e.message ?: "")))
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _weatherLiveData.postValue(
                        Event(
                            State.error(
                                e.message ?: ""
                            )
                        )
                    )
                }
            }
        }
    }

    private suspend fun addWeatherDataIntoDb(weatherResponse: WeatherResponse) {
        val weatherData = WeatherData()
        weatherData.id = weatherResponse.id
        weatherData.icon = weatherResponse.weather.first().icon
        weatherData.cityName = weatherResponse.name.toLowerCase()
        weatherData.countryName = weatherResponse.sys.country
        weatherData.temp = weatherResponse.main.temp
        weatherData.dateTime = SimpleDateFormat("E, d MMM yyyy HH:mm:ss").format(Date())
        weatherData.humidity = weatherResponse.main.humidity
        weatherData.windSpeed = weatherResponse.wind.speed
        weatherData.visibility = weatherResponse.visibility
        weatherData.sunrise = weatherResponse.sys.sunrise
        weatherData.sunset = weatherResponse.sys.sunset
        repository.addWeather(weatherData)
    }

    fun fetchWeatherDataFromDb(cityName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val weatherData = repository.fetchWeatherData(cityName.toLowerCase())
            withContext(Dispatchers.Main) {
                if (weatherData != null) {
                    if (isTimeExpired(weatherData.dateTime)) {
                        findCityWeather(cityName)
                    } else {
                        _weatherLiveData.postValue(
                            Event(
                                State.success(
                                    weatherData
                                )
                            )
                        )
                    }

                } else {
                    findCityWeather(cityName)
                }

            }
        }
    }

    private fun isTimeExpired(dateTimeSavedWeather: String?): Boolean {
        dateTimeSavedWeather?.let {
            val currentDateTime = Date()
            val savedWeatherDateTime =
                SimpleDateFormat("E, d MMM yyyy HH:mm:ss").parse(it)
            val diff: Long = currentDateTime.time - savedWeatherDateTime.time
            val seconds = diff / 1000
            val minutes = seconds / 60
            if (minutes > 10)
                return true
        }
        return false
    }
}
