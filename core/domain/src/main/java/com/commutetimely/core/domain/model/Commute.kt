package com.commutetimely.core.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * Domain model representing a commute journey
 * 
 * This class contains all the essential information about a commute
 * including origin, destination, route details, and weather information.
 * 
 * @property id Unique identifier for the commute
 * @property origin Starting location of the commute
 * @property destination End location of the commute
 * @property departureTime Planned departure time
 * @property estimatedDuration Estimated travel time in minutes
 * @property distance Distance in kilometers
 * @property routeType Type of route (driving, walking, cycling, transit)
 * @property weatherInfo Current weather conditions for the commute
 * @property trafficLevel Current traffic conditions
 * @property isActive Whether this commute is currently active
 * @property createdAt When this commute was created
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@Parcelize
data class Commute(
    val id: String,
    val origin: Location,
    val destination: Location,
    val departureTime: Date,
    val estimatedDuration: Int, // in minutes
    val distance: Double, // in kilometers
    val routeType: RouteType,
    val weatherInfo: WeatherInfo?,
    val trafficLevel: TrafficLevel,
    val isActive: Boolean = false,
    val createdAt: Date = Date()
) : Parcelable

/**
 * Represents a geographical location
 */
@Parcelize
data class Location(
    val latitude: Double,
    val longitude: Double,
    val name: String,
    val address: String? = null
) : Parcelable

/**
 * Enumeration of available route types
 */
enum class RouteType {
    DRIVING,
    WALKING,
    CYCLING,
    TRANSIT
}

/**
 * Enumeration of traffic levels
 */
enum class TrafficLevel {
    LOW,
    MEDIUM,
    HIGH,
    SEVERE
}
