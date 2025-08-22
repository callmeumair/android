package com.commutetimely.feature.weather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.commutetimely.core.domain.model.WeatherInfo
import com.commutetimely.core.domain.model.WeatherForecast
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor() : ViewModel() {
    
    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()
    
    fun loadCurrentWeather() {
        viewModelScope.launch {
            // TODO: Load actual weather data
            _uiState.value = _uiState.value.copy(
                currentWeather = createSampleWeatherInfo()
            )
        }
    }
    
    fun loadWeatherForecast() {
        viewModelScope.launch {
            // TODO: Load actual forecast data
            _uiState.value = _uiState.value.copy(
                hourlyForecast = createSampleHourlyForecast(),
                dailyForecast = createSampleDailyForecast()
            )
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
