package com.commutetimely.feature.weather.presentation.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp

@Composable
fun WeatherIcon(
    iconCode: String,
    size: Dp,
    modifier: Modifier = Modifier
) {
    // TODO: Implement proper weather icon mapping based on iconCode
    // For now, use a default sunny icon
    Icon(
        imageVector = Icons.Default.WbSunny,
        contentDescription = "Weather Icon",
        modifier = modifier.size(size),
        tint = Color(0xFFFFB300) // Sunny yellow color
    )
}
