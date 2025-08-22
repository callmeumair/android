package com.commutetimely.core.data.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resumeWithException

/**
 * Service for handling location operations
 * 
 * This service provides functionality for:
 * - Getting current location
 * - Location updates
 * - Permission checking
 * - Location availability checks
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@Singleton
class LocationService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient: FusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }

    /**
     * Check if location permissions are granted
     */
    fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Get current location as a suspend function
     */
    suspend fun getCurrentLocation(): com.commutetimely.core.domain.model.Location {
        if (!hasLocationPermission()) {
            throw SecurityException("Location permission not granted")
        }

        return suspendCancellableCoroutine { continuation ->
            try {
                val locationRequest = LocationRequest.Builder(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    1000L
                ).apply {
                    setMaxUpdates(1)
                    setWaitForAccurateLocation(false)
                }.build()

                val locationCallback = object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        result.lastLocation?.let { location ->
                            val domainLocation = com.commutetimely.core.domain.model.Location(
                                latitude = location.latitude,
                                longitude = location.longitude,
                                name = "Current Location",
                                address = null
                            )
                            continuation.resumeWith(Result.success(domainLocation))
                        } ?: continuation.resumeWithException(
                            Exception("Unable to get current location")
                        )
                        fusedLocationClient.removeLocationUpdates(this)
                    }
                }

                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )

                continuation.invokeOnCancellation {
                    fusedLocationClient.removeLocationUpdates(locationCallback)
                }

            } catch (e: SecurityException) {
                continuation.resumeWithException(e)
            } catch (e: Exception) {
                continuation.resumeWithException(e)
            }
        }
    }

    /**
     * Get last known location quickly
     */
    suspend fun getLastKnownLocation(): com.commutetimely.core.domain.model.Location? {
        if (!hasLocationPermission()) {
            return null
        }

        return try {
            val location = fusedLocationClient.lastLocation.await()
            location?.let {
                com.commutetimely.core.domain.model.Location(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    name = "Last Known Location",
                    address = null
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Start location updates as a Flow
     */
    fun getLocationUpdates(intervalMs: Long = 5000L): Flow<com.commutetimely.core.domain.model.Location> {
        return callbackFlow {
            if (!hasLocationPermission()) {
                close(SecurityException("Location permission not granted"))
                return@callbackFlow
            }

            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                intervalMs
            ).build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    result.lastLocation?.let { location ->
                        val domainLocation = com.commutetimely.core.domain.model.Location(
                            latitude = location.latitude,
                            longitude = location.longitude,
                            name = "Current Location",
                            address = null
                        )
                        trySend(domainLocation)
                    }
                }
            }

            try {
                fusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback,
                    Looper.getMainLooper()
                )
            } catch (e: SecurityException) {
                close(e)
                return@callbackFlow
            }

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }

    /**
     * Check if location services are enabled
     */
    fun isLocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as android.location.LocationManager
        return locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER) ||
               locationManager.isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)
    }
}

/**
 * Extension function to convert Task to suspend function
 */
private suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { continuation ->
        addOnCompleteListener { task ->
            if (task.exception != null) {
                continuation.resumeWithException(task.exception!!)
            } else {
                continuation.resumeWith(Result.success(task.result))
            }
        }
    }
}
