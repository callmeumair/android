package com.commutetimely.feature.map.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commutetimely.core.data.location.LocationService
import com.commutetimely.core.domain.model.Commute
import com.commutetimely.core.domain.model.Location
import com.commutetimely.core.domain.model.RouteType
import com.commutetimely.core.domain.model.WeatherInfo
import com.commutetimely.core.domain.repository.CommuteRepository
import com.commutetimely.core.domain.repository.WeatherRepository
import com.commutetimely.core.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val locationService: LocationService,
    private val commuteRepository: CommuteRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()
    
    fun updateOrigin(origin: String) {
        _uiState.value = _uiState.value.copy(origin = origin)
    }
    
    fun updateDestination(destination: String) {
        _uiState.value = _uiState.value.copy(destination = destination)
    }
    
    fun updateRouteType(routeType: RouteType) {
        _uiState.value = _uiState.value.copy(selectedRouteType = routeType)
    }
    
    fun planRoute(origin: String, destination: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                // Geocode origin and destination
                val originResult = commuteRepository.geocodeAddress(origin)
                val destinationResult = commuteRepository.geocodeAddress(destination)
                
                when {
                    originResult is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to find origin: ${originResult.message}"
                        )
                        return@launch
                    }
                    destinationResult is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to find destination: ${destinationResult.message}"
                        )
                        return@launch
                    }
                    originResult is Resource.Success && destinationResult is Resource.Success -> {
                        val originLocation = originResult.data.firstOrNull()
                        val destinationLocation = destinationResult.data.firstOrNull()
                        
                        if (originLocation == null || destinationLocation == null) {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = "Could not find valid locations"
                            )
                            return@launch
                        }
                        
                        // Plan the route
                        val routeResult = commuteRepository.planRoute(
                            origin = originLocation,
                            destination = destinationLocation,
                            routeType = _uiState.value.selectedRouteType
                        )
                        
                        when (routeResult) {
                            is Resource.Success -> {
                                // Get weather info for the destination
                                val weatherResult = weatherRepository.getCurrentWeather(
                                    latitude = destinationLocation.latitude,
                                    longitude = destinationLocation.longitude
                                )
                                
                                val weatherInfo = if (weatherResult is Resource.Success) {
                                    weatherResult.data
                                } else null
                                
                                val routeWithWeather = routeResult.data.copy(weatherInfo = weatherInfo)
                                
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    currentRoute = routeWithWeather,
                                    weatherInfo = weatherInfo
                                )
                            }
                            is Resource.Error -> {
                                _uiState.value = _uiState.value.copy(
                                    isLoading = false,
                                    error = "Failed to plan route: ${routeResult.message}"
                                )
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "An error occurred: ${e.message}"
                )
            }
        }
    }
    
    fun getCurrentLocation() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                if (!locationService.hasLocationPermission()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Location permission not granted"
                    )
                    return@launch
                }
                
                val currentLocation = locationService.getCurrentLocation()
                
                // Reverse geocode to get address
                val addressResult = commuteRepository.reverseGeocode(
                    latitude = currentLocation.latitude,
                    longitude = currentLocation.longitude
                )
                
                val locationWithAddress = when (addressResult) {
                    is Resource.Success -> addressResult.data
                    is Resource.Error -> currentLocation
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    currentLocation = locationWithAddress,
                    origin = locationWithAddress.name
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get current location: ${e.message}"
                )
            }
        }
    }
    
    fun searchDestinations(query: String) {
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList())
            return
        }
        
        viewModelScope.launch {
            val result = commuteRepository.geocodeAddress(query)
            when (result) {
                is Resource.Success -> {
                    _uiState.value = _uiState.value.copy(searchResults = result.data)
                }
                is Resource.Error -> {
                    _uiState.value = _uiState.value.copy(
                        searchResults = emptyList(),
                        error = "Search failed: ${result.message}"
                    )
                }
            }
        }
    }
    
    fun selectDestination(location: Location) {
        _uiState.value = _uiState.value.copy(
            destination = location.name,
            searchResults = emptyList()
        )
    }
    
    fun displayRouteOnMap(map: Any, route: Commute) {
        // TODO: Implement map route display with Mapbox
        // This would involve drawing the route geometry on the map
        // and adding markers for origin and destination
    }
    
    fun startNavigation(routeId: String) {
        viewModelScope.launch {
            _uiState.value.currentRoute?.let { route ->
                _uiState.value = _uiState.value.copy(
                    currentRoute = route.copy(isActive = true)
                )
                // TODO: Start actual navigation using Mapbox Navigation SDK
            }
        }
    }
    
    fun retryLastOperation() {
        // Retry the last failed operation
        _uiState.value = _uiState.value.copy(error = null)
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class MapUiState(
    val origin: String = "",
    val destination: String = "",
    val selectedRouteType: RouteType = RouteType.DRIVING,
    val currentRoute: Commute? = null,
    val currentLocation: Location? = null,
    val weatherInfo: WeatherInfo? = null,
    val trafficInfo: Any? = null,
    val searchResults: List<Location> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
