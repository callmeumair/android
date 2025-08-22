package com.commutetimely.feature.weather.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commutetimely.core.domain.model.WeatherInfo
import com.commutetimely.core.domain.model.WeatherForecast
import com.commutetimely.core.domain.model.CommuteImpact
import com.commutetimely.core.ui.theme.*
import com.commutetimely.feature.weather.presentation.components.*

/**
 * Weather Summary Screen for CommuteTimely
 * 
 * This screen provides comprehensive weather information including:
 * - Current weather conditions
 * - Hourly and daily forecasts
 * - Weather alerts and warnings
 * - Commute impact analysis
 * - Air quality information
 * 
 * @param onNavigateBack Navigation callback to go back
 * @param onNavigateToMapView Navigation callback to map view
 * @param viewModel The weather view model
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSummaryScreen(
    onNavigateBack: () -> Unit,
    onNavigateToMapView: (String) -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadCurrentWeather()
        viewModel.loadWeatherForecast()
    }
    
    Scaffold(
        topBar = {
            WeatherTopBar(
                onNavigateBack = onNavigateBack,
                onRefreshClick = { viewModel.refreshWeather() },
                onSettingsClick = { /* TODO: Navigate to weather settings */ }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Current Weather Card
            item {
                uiState.currentWeather?.let { weather ->
                    CurrentWeatherCard(
                        weatherInfo = weather,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Commute Impact Analysis
            item {
                uiState.currentWeather?.let { weather ->
                    CommuteImpactCard(
                        weatherInfo = weather,
                        onViewMapClick = { location ->
                            onNavigateToMapView(location)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Hourly Forecast
            item {
                if (uiState.hourlyForecast.isNotEmpty()) {
                    HourlyForecastSection(
                        hourlyForecast = uiState.hourlyForecast,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Daily Forecast
            item {
                if (uiState.dailyForecast.isNotEmpty()) {
                    DailyForecastSection(
                        dailyForecast = uiState.dailyForecast,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Air Quality Information
            item {
                uiState.airQuality?.let { airQuality ->
                    AirQualityCard(
                        airQuality = airQuality,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Weather Alerts
            item {
                if (uiState.weatherAlerts.isNotEmpty()) {
                    WeatherAlertsSection(
                        alerts = uiState.weatherAlerts,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Detailed Weather Information
            item {
                uiState.currentWeather?.let { weather ->
                    DetailedWeatherInfoCard(
                        weatherInfo = weather,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Loading State
            if (uiState.isLoading) {
                item {
                    WeatherLoadingIndicator(
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
            
            // Error State
            uiState.error?.let { error ->
                item {
                    WeatherErrorMessage(
                        error = error,
                        onRetry = { viewModel.retryLastOperation() },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/**
 * Top bar for the weather screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun WeatherTopBar(
    onNavigateBack: () -> Unit,
    onRefreshClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Weather Summary",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onNavigateBack) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onRefreshClick) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refresh Weather"
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Weather Settings"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

/**
 * Current weather display card
 */
@Composable
private fun CurrentWeatherCard(
    weatherInfo: WeatherInfo,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = weatherCardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Weather Icon
            WeatherIcon(
                iconCode = weatherInfo.iconCode,
                size = 80.dp,
                modifier = Modifier.size(80.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Temperature
            Text(
                text = "${weatherInfo.temperature.toInt()}°C",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            // Weather Description
            Text(
                text = weatherInfo.weatherCondition,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Additional Weather Details
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                WeatherDetailItem(
                    icon = Icons.Default.Thermostat,
                    label = "Feels like",
                    value = "${weatherInfo.feelsLike.toInt()}°C"
                )
                
                WeatherDetailItem(
                    icon = Icons.Default.Opacity,
                    label = "Humidity",
                    value = "${weatherInfo.humidity}%"
                )
                
                WeatherDetailItem(
                    icon = Icons.Default.Air,
                    label = "Wind",
                    value = "${weatherInfo.windSpeed} km/h"
                )
            }
        }
    }
}

/**
 * Commute impact analysis card
 */
@Composable
private fun CommuteImpactCard(
    weatherInfo: WeatherInfo,
    onViewMapClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Commute Impact",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Impact Level
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                val impactColor = when (weatherInfo.commuteImpact) {
                    CommuteImpact.GOOD -> commute_success
                    CommuteImpact.MODERATE -> commute_warning
                    CommuteImpact.POOR -> commute_error
                }
                
                Icon(
                    imageVector = when (weatherInfo.commuteImpact) {
                        CommuteImpact.GOOD -> Icons.Default.CheckCircle
                        CommuteImpact.MODERATE -> Icons.Default.Warning
                        CommuteImpact.POOR -> Icons.Default.Error
                    },
                    contentDescription = null,
                    tint = impactColor,
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = when (weatherInfo.commuteImpact) {
                        CommuteImpact.GOOD -> "Good conditions for commute"
                        CommuteImpact.MODERATE -> "Moderate impact expected"
                        CommuteImpact.POOR -> "Poor conditions - consider alternatives"
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Recommendations
            Text(
                text = "Recommendations:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            val recommendations = when (weatherInfo.commuteImpact) {
                CommuteImpact.GOOD -> listOf(
                    "Normal commute time expected",
                    "Standard route recommended"
                )
                CommuteImpact.MODERATE -> listOf(
                    "Allow extra travel time",
                    "Check for alternative routes"
                )
                CommuteImpact.POOR -> listOf(
                    "Significant delays expected",
                    "Consider public transit",
                    "Check weather alerts"
                )
            }
            
            recommendations.forEach { recommendation ->
                Text(
                    text = "• $recommendation",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Button(
                onClick = { onViewMapClick("current_location") },
                modifier = Modifier.fillMaxWidth(),
                shape = buttonShape
            ) {
                Icon(
                    imageVector = Icons.Default.Map,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("View on Map")
            }
        }
    }
}

/**
 * Weather detail item component
 */
@Composable
private fun WeatherDetailItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        
        Spacer(modifier = Modifier.height(4.dp))
        
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
