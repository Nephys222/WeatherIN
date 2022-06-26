package com.nilearning.weatherin.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nilearning.weatherin.data.repository.WeatherRepository

class WeatherViewModelFactory(
    private val repository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return WeatherViewModel(repository) as T
    }
}
