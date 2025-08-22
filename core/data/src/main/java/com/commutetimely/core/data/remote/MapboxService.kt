package com.commutetimely.core.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit service interface for Mapbox API
 * 
 * This service provides access to all Mapbox features including:
 * - Directions and routing
 * - Geocoding and reverse geocoding
 * - Traffic information
 * - Map tiles and styles
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
interface MapboxService {
    
    /**
     * Get directions between two points
     * 
     * @param profile Routing profile (driving, walking, cycling, driving-traffic)
     * @param coordinates Coordinates in format: lon1,lat1;lon2,lat2
     * @param alternatives Whether to return alternative routes
     * @param steps Whether to return step-by-step instructions
     * @param annotations Additional metadata (duration, distance, speed)
     * @param overview Route overview type (full, simplified, false)
     * @param accessToken Mapbox access token
     * @return DirectionsResponse containing route information
     */
    @GET("directions/v5/mapbox/{profile}/{coordinates}")
    suspend fun getDirections(
        @Query("profile") profile: String,
        @Query("coordinates") coordinates: String,
        @Query("alternatives") alternatives: Boolean = true,
        @Query("steps") steps: Boolean = true,
        @Query("annotations") annotations: String = "duration,distance,speed",
        @Query("overview") overview: String = "full",
        @Query("access_token") accessToken: String
    ): DirectionsResponse
    
    /**
     * Get traffic information for a route
     * 
     * @param coordinates Coordinates in format: lon1,lat1;lon2,lat2
     * @param accessToken Mapbox access token
     * @return TrafficResponse containing traffic data
     */
    @GET("directions-matrix/v1/mapbox/driving/{coordinates}")
    suspend fun getTrafficMatrix(
        @Query("coordinates") coordinates: String,
        @Query("access_token") accessToken: String
    ): TrafficResponse
    
    /**
     * Geocode an address to coordinates
     * 
     * @param searchText Address or place name to search for
     * @param types Type of results to return
     * @param limit Maximum number of results
     * @param accessToken Mapbox access token
     * @return GeocodingResponse containing location results
     */
    @GET("geocoding/v5/mapbox.places/{searchText}.json")
    suspend fun geocode(
        @Query("searchText") searchText: String,
        @Query("types") types: String = "poi,place,neighborhood,address",
        @Query("limit") limit: Int = 5,
        @Query("access_token") accessToken: String
    ): GeocodingResponse
    
    /**
     * Reverse geocode coordinates to address
     * 
     * @param longitude Longitude coordinate
     * @param latitude Latitude coordinate
     * @param types Type of results to return
     * @param accessToken Mapbox access token
     * @return GeocodingResponse containing address results
     */
    @GET("geocoding/v5/mapbox.places/{longitude},{latitude}.json")
    suspend fun reverseGeocode(
        @Query("longitude") longitude: Double,
        @Query("latitude") latitude: Double,
        @Query("types") types: String = "poi,place,neighborhood,address",
        @Query("access_token") accessToken: String
    ): GeocodingResponse
}

/**
 * Response model for directions API
 */
data class DirectionsResponse(
    val routes: List<Route>,
    val waypoints: List<Waypoint>,
    val code: String,
    val uuid: String
)

/**
 * Response model for traffic matrix API
 */
data class TrafficResponse(
    val durations: List<List<Double>>,
    val distances: List<List<Double>>,
    val destinations: List<Location>,
    val sources: List<Location>
)

/**
 * Response model for geocoding API
 */
data class GeocodingResponse(
    val type: String,
    val query: List<String>,
    val features: List<Feature>,
    val attribution: String
)

/**
 * Route information from directions API
 */
data class Route(
    val geometry: String,
    val legs: List<Leg>,
    val weight_name: String,
    val weight: Double,
    val duration: Double,
    val distance: Double
)

/**
 * Route leg information
 */
data class Leg(
    val steps: List<Step>,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val summary: String
)

/**
 * Route step information
 */
data class Step(
    val geometry: String,
    val maneuver: Maneuver,
    val weight: Double,
    val duration: Double,
    val distance: Double,
    val name: String
)

/**
 * Maneuver information
 */
data class Maneuver(
    val instruction: String,
    val bearing_before: Int,
    val bearing_after: Int,
    val location: List<Double>,
    val type: String
)

/**
 * Waypoint information
 */
data class Waypoint(
    val distance: Double,
    val name: String,
    val location: List<Double>
)

/**
 * Location information
 */
data class Location(
    val name: String,
    val location: List<Double>
)

/**
 * Geocoding feature
 */
data class Feature(
    val id: String,
    val type: String,
    val place_type: List<String>,
    val relevance: Double,
    val properties: Map<String, Any>,
    val text: String,
    val place_name: String,
    val bbox: List<Double>?,
    val center: List<Double>,
    val geometry: Geometry
)

/**
 * Geometry information
 */
data class Geometry(
    val type: String,
    val coordinates: List<Double>
)
