package com.nilearning.weatherin.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.nilearning.weatherin.R
import com.nilearning.weatherin.databinding.ActivityWeatherinBinding
import com.nilearning.weatherin.util.EventObserver
import com.nilearning.weatherin.util.State
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import kotlin.math.roundToInt

class WeatherinActivity : AppCompatActivity(), KodeinAware {

    override val kodein by closestKodein()
    private lateinit var binding: ActivityWeatherinBinding
    private val viewModelFactory: WeatherViewModelFactory by instance()

    private val niViewModel: WeatherViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(WeatherViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_weatherin)
        startUI()
        callWeatherAPI()
    }

    private fun startUI() {
        binding.searchText.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                niViewModel.fetchWeatherDataFromDb((view as EditText).text.toString())
            }
            false
        }
    }

    private fun callWeatherAPI() {
        niViewModel.weatherLiveData.observe(this, EventObserver { state ->
            when (state) {
                is State.Loading -> {
                }
                is State.Success -> {
                    binding.intro.visibility = View.GONE
                    binding.mainView.visibility = View.VISIBLE
                    binding.searchText.text?.clear()
                    state.data.let { weatherData ->
                        val iconCode = weatherData.icon?.replace("n", "d")
                        Glide.with(
                            binding.imgSun).load(
                            "http://openweathermap.org/img/wn/" + "${iconCode}@4x.png"
                        ).thumbnail(1f).into(binding.imgSun)
                        binding.textTemp.text = weatherData.temp?.roundToInt().toString()
                        binding.cityName.text =
                            "${weatherData.cityName?.capitalize()}, ${weatherData.countryName}"
                        binding.textHumid.text = "${weatherData.humidity.toString()} %"
                        binding.textWinds.text = "${weatherData.windSpeed?.times(3.6)?.roundToInt().toString()} KMH"
                        binding.textVisible.text = "${weatherData.visibility?.div(1000).toString()} KM"
                        binding.textRise.text =
                            SimpleDateFormat("HH:mm").format(weatherData.sunrise?.times(1000))
                        binding.textSunset.text =
                            SimpleDateFormat("HH:mm").format(weatherData.sunset?.times(1000))
                    }

                }
                is State.Error -> {
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        })
    }
}