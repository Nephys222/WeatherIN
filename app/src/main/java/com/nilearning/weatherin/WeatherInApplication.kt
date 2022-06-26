package com.nilearning.weatherin

import android.app.Application
import com.nilearning.weatherin.data.repository.WeatherRepository
import com.nilearning.weatherin.data.room.WeatherDatabase
import com.nilearning.weatherin.data.service.ConnectionInterceptor
import com.nilearning.weatherin.data.service.WeatherApi
import com.nilearning.weatherin.ui.WeatherViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class WeatherInApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherInApplication))

        bind() from singleton { ConnectionInterceptor(instance()) }
        bind() from singleton { WeatherApi(instance()) }
        bind() from singleton { WeatherRepository(instance(), instance()) }
        bind() from provider { WeatherViewModelFactory(instance()) }
        bind() from provider { WeatherDatabase(instance()) }
    }


}
