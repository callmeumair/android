package com.commutetimely.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for Weatherbit API
 * 
 * This service provides access to weather data including:
 * - Current weather conditions
 * - Hourly and daily forecasts
 * - Air quality information
 * - Weather alerts
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
interface WeatherbitService {
    
    /**
     * Get current weather for a specific location
     * 
     * @param lat Latitude coordinate
     * @param lon Longitude coordinate
     * @param key API key for Weatherbit
     * @param units Units for measurements (M for metric, I for imperial)
     * @return CurrentWeatherResponse containing current weather data
     */
    @GET("v2.0/current")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key: String,
        @Query("units") units: String = "M"
    ): CurrentWeatherResponse
    
    /**
     * Get hourly weather forecast
     * 
     * @param lat Latitude coordinate
     * @param lon Longitude coordinate
     * @param key API key for Weatherbit
     * @param hours Number of hours to forecast (1-48)
     * @param units Units for measurements
     * @return HourlyForecastResponse containing hourly forecast data
     */
    @GET("v2.0/forecast/hourly")
    suspend fun getHourlyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key: String,
        @Query("hours") hours: Int = 24,
        @Query("units") units: String = "M"
    ): HourlyForecastResponse
    
    /**
     * Get daily weather forecast
     * 
     * @param lat Latitude coordinate
     * @param lon Longitude coordinate
     * @param key API key for Weatherbit
     * @param days Number of days to forecast (1-16)
     * @param units Units for measurements
     * @return DailyForecastResponse containing daily forecast data
     */
    @GET("v2.0/forecast/daily")
    suspend fun getDailyForecast(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key: String,
        @Query("days") days: Int = 7,
        @Query("units") units: String = "M"
    ): DailyForecastResponse
    
    /**
     * Get air quality information
     * 
     * @param lat Latitude coordinate
     * @param lon Longitude coordinate
     * @param key API key for Weatherbit
     * @return AirQualityResponse containing air quality data
     */
    @GET("v2.0/current/airquality")
    suspend fun getAirQuality(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key: String
    ): AirQualityResponse
    
    /**
     * Get weather alerts for a location
     * 
     * @param lat Latitude coordinate
     * @param lon Longitude coordinate
     * @param key API key for Weatherbit
     * @return AlertsResponse containing weather alerts
     */
    @GET("v2.0/alerts")
    suspend fun getAlerts(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") key: String
    ): AlertsResponse
}

/**
 * Response model for current weather API
 */
data class CurrentWeatherResponse(
    val count: Int,
    val data: List<CurrentWeatherData>
)

/**
 * Current weather data
 */
data class CurrentWeatherData(
    val lat: Double,
    val lon: Double,
    val app_temp: Double,
    val aqi: Int,
    val city_name: String,
    val clouds: Int,
    val country_code: String,
    val datetime: String,
    val dewpt: Double,
    val dhi: Double,
    val dni: Double,
    val elev_angle: Double,
    val ghi: Double,
    val gust: Double?,
    val h_angle: Int,
    val precip: Double,
    val pres: Double,
    val rh: Int,
    val slp: Double,
    val snow: Double,
    val solar_rad: Double,
    val state_code: String,
    val station: String,
    val sunrise: String,
    val sunset: String,
    val temp: Double,
    val timezone: String,
    val ts: Long,
    val uv: Double,
    val vis: Double,
    val weather: Weather,
    val wind_cdir: String,
    val wind_cdir_full: String,
    val wind_dir: Int,
    val wind_spd: Double
)

/**
 * Weather condition information
 */
data class Weather(
    val icon: String,
    val code: Int,
    val description: String
)

/**
 * Response model for hourly forecast API
 */
data class HourlyForecastResponse(
    val count: Int,
    val data: List<HourlyForecastData>
)

/**
 * Hourly forecast data
 */
data class HourlyForecastData(
    val timestamp_utc: String,
    val timestamp_local: String,
    val temp: Double,
    val app_temp: Double,
    val rh: Int,
    val dewpt: Double,
    val clouds: Int,
    val vis: Double,
    val wind_spd: Double,
    val wind_dir: Int,
    val wind_cdir: String,
    val precip: Double,
    val snow: Double,
    val weather: Weather,
    val pop: Int
)

/**
 * Response model for daily forecast API
 */
data class DailyForecastResponse(
    val count: Int,
    val data: List<DailyForecastData>
)

/**
 * Daily forecast data
 */
data class DailyForecastData(
    val valid_date: String,
    val ts: Long,
    val datetime: String,
    val wind_gust_spd: Double,
    val wind_spd: Double,
    val wind_dir: Int,
    val wind_cdir: String,
    val temp: Double,
    val max_temp: Double,
    val min_temp: Double,
    val high_temp: Double,
    val low_temp: Double,
    val app_high_temp: Double,
    val app_low_temp: Double,
    val rh: Int,
    val dewpt: Double,
    val clouds: Int,
    val vis: Double,
    val precip: Double,
    val snow: Double,
    val weather: Weather,
    val pop: Int,
    val moon_phase: Double,
    val sunrise_ts: Long,
    val sunset_ts: Long,
    val moonrise_ts: Long,
    val moonset_ts: Long
)

/**
 * Response model for air quality API
 */
data class AirQualityResponse(
    val count: Int,
    val data: List<AirQualityData>
)

/**
 * Air quality data
 */
data class AirQualityData(
    val lat: Double,
    val lon: Double,
    val aqi: Int,
    val city_name: String,
    val country_code: String,
    val state_code: String,
    val timezone: String,
    val ts: Long,
    val o3: Double,
    val so2: Double,
    val no2: Double,
    val co: Double,
    val pm10: Double,
    val pm25: Double
)

/**
 * Response model for alerts API
 */
data class AlertsResponse(
    val count: Int,
    val data: List<AlertData>
)

/**
 * Weather alert data
 */
data class AlertData(
    val lat: Double,
    val lon: Double,
    val city_name: String,
    val country_code: String,
    val state_code: String,
    val timezone: String,
    val alerts: List<Alert>
)

/**
 * Individual weather alert
 */
data class Alert(
    val title: String,
    val description: String,
    val severity: String,
    val effective_utc: String,
    val expires_utc: String,
    val onset_utc: String,
    val regions: List<String>,
    val certainty: String,
    val urgency: String
)
