package com.nilearning.weatherin.data.model


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

@Keep
data class Wind(
    @SerializedName("deg")
    val deg: Int,
    @SerializedName("speed")
    val speed: Double
)