package com.commutetimely.core.data.commute

import com.commutetimely.core.data.remote.MapboxService
import com.commutetimely.core.data.remote.DirectionsResponse
import com.commutetimely.core.domain.model.Commute
import com.commutetimely.core.domain.model.Location
import com.commutetimely.core.domain.model.RouteType
import com.commutetimely.core.domain.model.TrafficLevel
import com.commutetimely.core.domain.repository.CommuteRepository
import com.commutetimely.core.domain.util.Resource
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementation of CommuteRepository using Mapbox API
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@Singleton
class CommuteRepositoryImpl @Inject constructor(
    private val mapboxService: MapboxService,
    private val accessToken: String
) : CommuteRepository {

    override suspend fun planRoute(
        origin: Location,
        destination: Location,
        routeType: RouteType
    ): Resource<Commute> {
        return try {
            val profile = when (routeType) {
                RouteType.DRIVING -> "driving-traffic"
                RouteType.WALKING -> "walking"
                RouteType.CYCLING -> "cycling"
                RouteType.TRANSIT -> "driving-traffic" // Fallback to driving for now
            }

            val coordinates = "${origin.longitude},${origin.latitude};${destination.longitude},${destination.latitude}"

            val response = mapboxService.getDirections(
                profile = profile,
                coordinates = coordinates,
                accessToken = accessToken
            )

            if (response.routes.isNotEmpty()) {
                val route = response.routes.first()
                val commute = Commute(
                    id = "route_${System.currentTimeMillis()}",
                    origin = origin,
                    destination = destination,
                    departureTime = Date(),
                    estimatedDuration = (route.duration / 60).toInt(), // Convert seconds to minutes
                    distance = route.distance / 1000.0, // Convert meters to kilometers
                    routeType = routeType,
                    weatherInfo = null, // Will be populated separately
                    trafficLevel = calculateTrafficLevel(route.duration),
                    isActive = false
                )
                Resource.Success(commute)
            } else {
                Resource.Error("No routes found")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to plan route: ${e.message}")
        }
    }

    override suspend fun geocodeAddress(address: String): Resource<List<Location>> {
        return try {
            val response = mapboxService.geocode(
                searchText = address,
                accessToken = accessToken
            )

            val locations = response.features.map { feature ->
                Location(
                    latitude = feature.center[1], // Latitude is second in the array
                    longitude = feature.center[0], // Longitude is first in the array
                    name = feature.text,
                    address = feature.place_name
                )
            }

            Resource.Success(locations)
        } catch (e: Exception) {
            Resource.Error("Failed to geocode address: ${e.message}")
        }
    }

    override suspend fun reverseGeocode(
        latitude: Double,
        longitude: Double
    ): Resource<Location> {
        return try {
            val response = mapboxService.reverseGeocode(
                longitude = longitude,
                latitude = latitude,
                accessToken = accessToken
            )

            if (response.features.isNotEmpty()) {
                val feature = response.features.first()
                val location = Location(
                    latitude = latitude,
                    longitude = longitude,
                    name = feature.text,
                    address = feature.place_name
                )
                Resource.Success(location)
            } else {
                Resource.Error("No address found for coordinates")
            }
        } catch (e: Exception) {
            Resource.Error("Failed to reverse geocode: ${e.message}")
        }
    }

    override suspend fun getCommutes(): Resource<List<Commute>> {
        // For now, return empty list as we don't have local storage implemented
        // In a real app, this would fetch from local database
        return Resource.Success(emptyList())
    }

    override suspend fun saveCommute(commute: Commute): Resource<Unit> {
        // For now, just return success
        // In a real app, this would save to local database
        return Resource.Success(Unit)
    }

    override suspend fun deleteCommute(commuteId: String): Resource<Unit> {
        // For now, just return success
        // In a real app, this would delete from local database
        return Resource.Success(Unit)
    }

    private fun calculateTrafficLevel(durationSeconds: Double): TrafficLevel {
        // This is a simple heuristic - in a real app, you'd use traffic data
        return when {
            durationSeconds < 1800 -> TrafficLevel.LOW // Less than 30 minutes
            durationSeconds < 3600 -> TrafficLevel.MEDIUM // 30-60 minutes
            durationSeconds < 5400 -> TrafficLevel.HIGH // 1-1.5 hours
            else -> TrafficLevel.SEVERE // More than 1.5 hours
        }
    }
}
