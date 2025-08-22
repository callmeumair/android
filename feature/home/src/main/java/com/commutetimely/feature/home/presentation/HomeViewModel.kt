package com.commutetimely.feature.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commutetimely.core.data.location.LocationService
import com.commutetimely.core.domain.model.Commute
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
class HomeViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val commuteRepository: CommuteRepository,
    private val locationService: LocationService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        loadInitialData()
    }
    
    private fun loadInitialData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Load weather data if location permission is available
                if (locationService.hasLocationPermission()) {
                    val location = locationService.getCurrentLocation()
                    val weatherResult = weatherRepository.getCurrentWeather(
                        latitude = location.latitude,
                        longitude = location.longitude
                    )
                    
                    val weatherInfo = when (weatherResult) {
                        is Resource.Success -> weatherResult.data
                        is Resource.Error -> createSampleWeatherInfo() // Fallback
                    }
                    
                    _uiState.value = _uiState.value.copy(weatherInfo = weatherInfo)
                }
                
                // Load commute data
                val commutesResult = commuteRepository.getCommutes()
                val recentCommutes = when (commutesResult) {
                    is Resource.Success -> commutesResult.data
                    is Resource.Error -> createSampleCommutes() // Fallback
                }
                
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    recentCommutes = recentCommutes,
                    activeCommutes = recentCommutes.filter { it.isActive },
                    commuteInsights = generateCommuteInsights(recentCommutes),
                    trafficSummary = "Light traffic on your usual route"
                )
                
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    weatherInfo = createSampleWeatherInfo(),
                    recentCommutes = createSampleCommutes(),
                    activeCommutes = emptyList(),
                    commuteInsights = "Your commute time is typically 25 minutes",
                    trafficSummary = "Light traffic on your usual route",
                    error = "Failed to load data: ${e.message}"
                )
            }
        }
    }
    
    private fun generateCommuteInsights(commutes: List<Commute>): String {
        if (commutes.isEmpty()) {
            return "Start tracking your commutes to see insights"
        }
        
        val avgDuration = commutes.map { it.estimatedDuration }.average()
        return "Your average commute time is ${avgDuration.toInt()} minutes"
    }
    
    private fun createSampleWeatherInfo(): WeatherInfo {
        return WeatherInfo(
            temperature = 22.0,
            feelsLike = 24.0,
            humidity = 65,
            windSpeed = 12.0,
            windDirection = 180,
            precipitation = 0.0,
            weatherCondition = "Partly Cloudy",
            iconCode = "02d",
            visibility = 10.0,
            uvIndex = 5,
            airQuality = 45,
            commuteImpact = com.commutetimely.core.domain.model.CommuteImpact.GOOD
        )
    }
    
    private fun createSampleCommutes(): List<Commute> {
        return listOf(
            Commute(
                id = "1",
                origin = com.commutetimely.core.domain.model.Location(40.7128, -74.0060, "Home"),
                destination = com.commutetimely.core.domain.model.Location(40.7589, -73.9851, "Work"),
                departureTime = java.util.Date(System.currentTimeMillis() - 2 * 60 * 60 * 1000),
                estimatedDuration = 25,
                distance = 8.5,
                routeType = com.commutetimely.core.domain.model.RouteType.DRIVING,
                weatherInfo = null,
                trafficLevel = com.commutetimely.core.domain.model.TrafficLevel.LOW,
                isActive = false
            )
        )
    }
}

data class HomeUiState(
    val isLoading: Boolean = true,
    val weatherInfo: WeatherInfo? = null,
    val recentCommutes: List<Commute> = emptyList(),
    val activeCommutes: List<Commute> = emptyList(),
    val commuteInsights: String = "",
    val trafficSummary: String = "",
    val error: String? = null
)
