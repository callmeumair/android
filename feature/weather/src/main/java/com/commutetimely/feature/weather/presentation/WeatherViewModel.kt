package com.commutetimely.feature.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commutetimely.core.data.location.LocationService
import com.commutetimely.core.domain.model.WeatherInfo
import com.commutetimely.core.domain.model.WeatherForecast
import com.commutetimely.core.domain.repository.WeatherRepository
import com.commutetimely.core.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val locationService: LocationService
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    fun loadCurrentWeather() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            try {
                if (!locationService.hasLocationPermission()) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Location permission required for weather data"
                    )
                    return@launch
                }
                
                val location = locationService.getCurrentLocation()
                val weatherResult = weatherRepository.getCurrentWeather(
                    latitude = location.latitude,
                    longitude = location.longitude
                )
                
                when (weatherResult) {
                    is Resource.Success -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            currentWeather = weatherResult.data
                        )
                    }
                    is Resource.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = "Failed to load weather: ${weatherResult.message}"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to get location: ${e.message}"
                )
            }
        }
    }
    
    fun loadWeatherForecast() {
        viewModelScope.launch {
            try {
                if (!locationService.hasLocationPermission()) {
                    return@launch
                }
                
                val location = locationService.getCurrentLocation()
                val hourlyResult = weatherRepository.getHourlyForecast(
                    latitude = location.latitude,
                    longitude = location.longitude,
                    hours = 24
                )
                
                when (hourlyResult) {
                    is Resource.Success -> {
                        // Convert weather info to forecast format for UI
                        val hourlyForecast = hourlyResult.data.mapIndexed { index, weather ->
                            WeatherForecast(
                                date = "${index + 1}:00",
                                highTemp = weather.temperature,
                                lowTemp = weather.temperature - 2,
                                weatherCondition = weather.weatherCondition,
                                iconCode = weather.iconCode,
                                precipitation = weather.precipitation,
                                windSpeed = weather.windSpeed
                            )
                        }
                        
                        _uiState.value = _uiState.value.copy(
                            hourlyForecast = hourlyForecast
                        )
                    }
                    is Resource.Error -> {
                        // Use fallback data or show error
                        _uiState.value = _uiState.value.copy(
                            hourlyForecast = createSampleHourlyForecast()
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    hourlyForecast = createSampleHourlyForecast(),
                    dailyForecast = createSampleDailyForecast()
                )
            }
        }
    }
    
    fun refreshWeather() {
        loadCurrentWeather()
        loadWeatherForecast()
    }
    
    fun retryLastOperation() {
        // TODO: Implement retry logic
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
    
    private fun createSampleHourlyForecast(): List<WeatherForecast> {
        return listOf(
            WeatherForecast(
                date = "12:00",
                highTemp = 24.0,
                lowTemp = 20.0,
                weatherCondition = "Sunny",
                iconCode = "01d",
                precipitation = 0.0,
                windSpeed = 10.0
            )
        )
    }
    
    private fun createSampleDailyForecast(): List<WeatherForecast> {
        return listOf(
            WeatherForecast(
                date = "Today",
                highTemp = 26.0,
                lowTemp = 18.0,
                weatherCondition = "Partly Cloudy",
                iconCode = "02d",
                precipitation = 0.0,
                windSpeed = 12.0
            )
        )
    }
}

data class WeatherUiState(
    val currentWeather: WeatherInfo? = null,
    val hourlyForecast: List<WeatherForecast> = emptyList(),
    val dailyForecast: List<WeatherForecast> = emptyList(),
    val airQuality: Any? = null,
    val weatherAlerts: List<Any> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
