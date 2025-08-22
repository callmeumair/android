package com.commutetimely.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Domain model representing weather information
 * 
 * This class contains comprehensive weather data including current conditions,
 * forecasts, and relevant commute-impacting weather factors.
 * 
 * @property temperature Current temperature in Celsius
 * @property feelsLike Apparent temperature in Celsius
 * @property humidity Relative humidity percentage
 * @property windSpeed Wind speed in km/h
 * @property windDirection Wind direction in degrees
 * @property precipitation Probability of precipitation
 * @property weatherCondition Primary weather condition description
 * @property iconCode Weather icon identifier
 * @property visibility Visibility in kilometers
 * @property uvIndex UV index value
 * @property airQuality Air quality index
 * @property commuteImpact How weather affects commute (good, moderate, poor)
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@Parcelize
data class WeatherInfo(
    val temperature: Double,
    val feelsLike: Double,
    val humidity: Int,
    val windSpeed: Double,
    val windDirection: Int,
    val precipitation: Double,
    val weatherCondition: String,
    val iconCode: String,
    val visibility: Double,
    val uvIndex: Int,
    val airQuality: Int,
    val commuteImpact: CommuteImpact
) : Parcelable

/**
 * Enumeration of weather impact levels on commute
 */
enum class CommuteImpact {
    GOOD,      // Favorable conditions
    MODERATE,  // Some impact expected
    POOR       // Significant impact on commute
}

/**
 * Extended weather information for forecasts
 */
@Parcelize
data class WeatherForecast(
    val date: String,
    val highTemp: Double,
    val lowTemp: Double,
    val weatherCondition: String,
    val iconCode: String,
    val precipitation: Double,
    val windSpeed: Double
) : Parcelable
