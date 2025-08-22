package com.commutetimely.core.domain.repository

import com.commutetimely.core.domain.model.Location
import com.commutetimely.core.domain.model.WeatherInfo
import com.commutetimely.core.domain.model.WeatherForecast
import kotlinx.coroutines.flow.Flow

/**
 * Repository interface for weather-related operations
 * 
 * This interface defines the contract for all weather data operations
 * including current conditions, forecasts, and location-based weather.
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
interface WeatherRepository {
    
    /**
     * Get current weather for a specific location
     * 
     * @param location The location to get weather for
     * @return Current weather information
     */
    suspend fun getCurrentWeather(location: Location): WeatherInfo
    
    /**
     * Get weather forecast for a location
     * 
     * @param location The location to get forecast for
     * @param days Number of days to forecast (1-7)
     * @return List of weather forecasts
     */
    suspend fun getWeatherForecast(
        location: Location,
        days: Int = 7
    ): List<WeatherForecast>
    
    /**
     * Get weather updates as a Flow for real-time updates
     * 
     * @param location The location to monitor
     * @return Flow of weather updates
     */
    fun getWeatherUpdates(location: Location): Flow<WeatherInfo>
    
    /**
     * Get weather for multiple locations
     * 
     * @param locations List of locations to get weather for
     * @return Map of location to weather information
     */
    suspend fun getWeatherForMultipleLocations(
        locations: List<Location>
    ): Map<Location, WeatherInfo>
    
    /**
     * Get commute-specific weather information
     * 
     * @param origin Starting location
     * @param destination End location
     * @return Weather information relevant to the commute
     */
    suspend fun getCommuteWeather(
        origin: Location,
        destination: Location
    ): CommuteWeatherSummary
    
    /**
     * Check if weather conditions are suitable for commute
     * 
     * @param weatherInfo Current weather information
     * @param routeType Type of commute route
     * @return Whether conditions are suitable
     */
    suspend fun isWeatherSuitableForCommute(
        weatherInfo: WeatherInfo,
        routeType: String
    ): Boolean
}

/**
 * Summary of weather conditions for a commute route
 */
data class CommuteWeatherSummary(
    val originWeather: WeatherInfo,
    val destinationWeather: WeatherInfo,
    val routeWeather: List<WeatherInfo>,
    val overallImpact: String,
    val recommendations: List<String>
)
