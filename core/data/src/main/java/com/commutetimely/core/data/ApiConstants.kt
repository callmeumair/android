package com.commutetimely.core.data

/**
 * API Constants for CommuteTimely
 * 
 * This object contains all API keys and endpoints used throughout the application.
 * These values are configured at build time for security.
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
object ApiConstants {
    
    /**
     * Mapbox API configuration
     */
    object Mapbox {
        const val ACCESS_TOKEN = "pk.eyJ1IjoiY29tbXV0ZXRpbWVseSIsImEiOiJjbWUzMzUydmcwMmN1MmtzZnoycGs1ZDhhIn0.438vHnYipmUNS7JoCglyMg"
        const val BASE_URL = "https://api.mapbox.com/"
        const val DIRECTIONS_ENDPOINT = "directions/v5/mapbox/"
        const val GEOCODING_ENDPOINT = "geocoding/v5/mapbox.places/"
        const val MATRIX_ENDPOINT = "directions-matrix/v1/mapbox/"
    }
    
    /**
     * Weatherbit API configuration
     */
    object Weatherbit {
        const val API_KEY = "836afe5ccf9c46e1bc2fa3a894f676b3"
        const val BASE_URL = "https://api.weatherbit.io/"
        const val CURRENT_WEATHER_ENDPOINT = "v2.0/current"
        const val HOURLY_FORECAST_ENDPOINT = "v2.0/forecast/hourly"
        const val DAILY_FORECAST_ENDPOINT = "v2.0/forecast/daily"
        const val AIR_QUALITY_ENDPOINT = "v2.0/current/airquality"
        const val ALERTS_ENDPOINT = "v2.0/alerts"
    }
    
    /**
     * Default configuration values
     */
    object Defaults {
        const val WEATHER_UNITS = "M" // Metric units
        const val FORECAST_HOURS = 24
        const val FORECAST_DAYS = 7
        const val GEOCODING_LIMIT = 5
        const val ROUTING_PROFILE = "driving-traffic"
    }
}
