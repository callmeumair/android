package com.commutetimely.feature.weather.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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

/**
 * Simple Weather Screen - Functional implementation with sample data
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleWeatherScreen(
    onNavigateBack: () -> Unit
) {
    var currentLocation by remember { mutableStateOf("San Francisco, CA") }
    
    // Sample weather data
    val currentTemp = 22
    val weatherCondition = "Partly Cloudy"
    val humidity = 65
    val windSpeed = 8
    val visibility = 12
    val pressure = 1013
    
    val hourlyForecast = listOf(
        HourlyWeather("12:00", 22, Icons.Default.WbSunny),
        HourlyWeather("13:00", 24, Icons.Default.WbSunny),
        HourlyWeather("14:00", 26, Icons.Default.WbCloudy),
        HourlyWeather("15:00", 25, Icons.Default.WbCloudy),
        HourlyWeather("16:00", 23, Icons.Default.Thunderstorm),
        HourlyWeather("17:00", 21, Icons.Default.Thunderstorm)
    )
    
    val weeklyForecast = listOf(
        DailyWeather("Today", 26, 18, Icons.Default.WbSunny),
        DailyWeather("Tomorrow", 24, 16, Icons.Default.WbCloudy),
        DailyWeather("Wednesday", 22, 14, Icons.Default.Thunderstorm),
        DailyWeather("Thursday", 25, 17, Icons.Default.WbSunny),
        DailyWeather("Friday", 27, 19, Icons.Default.WbSunny)
    )
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weather") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Refresh weather */ }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Location selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = currentLocation,
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Tap to change location",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = { /* TODO: Change location */ }) {
                        Icon(Icons.Default.Edit, contentDescription = "Change Location")
                    }
                }
            }
            
            // Current weather
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.WbSunny,
                        contentDescription = "Weather",
                        modifier = Modifier.size(80.dp),
                        tint = Color(0xFFFF9800)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text(
                        text = "${currentTemp}°C",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    
                    Text(
                        text = weatherCondition,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Weather details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        WeatherDetailItem(
                            icon = Icons.Default.Water,
                            label = "Humidity",
                            value = "$humidity%"
                        )
                        WeatherDetailItem(
                            icon = Icons.Default.Air,
                            label = "Wind",
                            value = "$windSpeed km/h"
                        )
                        WeatherDetailItem(
                            icon = Icons.Default.Visibility,
                            label = "Visibility",
                            value = "$visibility km"
                        )
                    }
                }
            }
            
            // Hourly forecast
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Hourly Forecast",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(hourlyForecast) { weather ->
                            HourlyWeatherCard(weather)
                        }
                    }
                }
            }
            
            // Weekly forecast
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "7-Day Forecast",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    weeklyForecast.forEach { weather ->
                        DailyWeatherRow(weather)
                        if (weather != weeklyForecast.last()) {
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
            
            // Weather alerts/tips
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.Info,
                        contentDescription = "Info",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Commute Tip",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Perfect weather for your commute today! Light traffic expected.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }
    }
}

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
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HourlyWeatherCard(weather: HourlyWeather) {
    Card(
        modifier = Modifier.width(80.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = weather.time,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Icon(
                imageVector = weather.icon,
                contentDescription = "Weather",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${weather.temperature}°",
                style = MaterialTheme.typography.titleSmall
            )
        }
    }
}

@Composable
private fun DailyWeatherRow(weather: DailyWeather) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = weather.day,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )
        
        Icon(
            imageVector = weather.icon,
            contentDescription = "Weather",
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Text(
            text = "${weather.high}°",
            style = MaterialTheme.typography.titleSmall,
            modifier = Modifier.width(32.dp),
            textAlign = TextAlign.End
        )
        
        Text(
            text = " / ",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Text(
            text = "${weather.low}°",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(32.dp)
        )
    }
}

data class HourlyWeather(
    val time: String,
    val temperature: Int,
    val icon: ImageVector
)

data class DailyWeather(
    val day: String,
    val high: Int,
    val low: Int,
    val icon: ImageVector
)
