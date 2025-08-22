package com.commutetimely.feature.home.presentation

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commutetimely.core.domain.model.Commute
import com.commutetimely.core.domain.model.WeatherInfo
import com.commutetimely.core.domain.util.Resource
import com.commutetimely.core.ui.theme.*
import com.commutetimely.feature.home.presentation.components.*

/**
 * Main Home Screen for CommuteTimely
 * 
 * This screen serves as the primary dashboard showing:
 * - Current weather conditions
 * - Recent and active commutes
 * - Quick action buttons
 * - Commute insights and recommendations
 * 
 * @param onNavigateToCommutePlanner Navigation callback to commute planner
 * @param onNavigateToMapView Navigation callback to map view
 * @param onNavigateToWeatherSummary Navigation callback to weather summary
 * @param onNavigateToRouteDetails Navigation callback to route details
 * @param viewModel The home screen view model
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToCommutePlanner: () -> Unit,
    onNavigateToMapView: () -> Unit,
    onNavigateToWeatherSummary: () -> Unit,
    onNavigateToRouteDetails: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    Scaffold(
        topBar = {
            HomeTopBar(
                onProfileClick = { /* TODO: Navigate to profile */ },
                onNotificationsClick = { /* TODO: Navigate to notifications */ },
                onSettingsClick = { /* TODO: Navigate to settings */ }
            )
        },
        floatingActionButton = {
            HomeFloatingActionButton(
                onNewCommuteClick = onNavigateToCommutePlanner
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
            // Weather Summary Card
            item {
                AnimatedVisibility(
                    visible = uiState.weatherInfo != null,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    uiState.weatherInfo?.let { weather ->
                        WeatherSummaryCard(
                            weatherInfo = weather
                        )
                    }
                }
            }
            
            // Quick Actions
            item {
                QuickActionsSection(
                    onNavigateToMapView = onNavigateToMapView,
                    onNavigateToWeatherSummary = onNavigateToWeatherSummary
                )
            }
            
            // Active Commutes
            item {
                if (uiState.activeCommutes.isNotEmpty()) {
                    ActiveCommutesSection(
                        activeCommutes = uiState.activeCommutes,
                        onCommuteClick = { commute ->
                            onNavigateToRouteDetails(commute.id)
                        }
                    )
                }
            }
            
            // Recent Commutes
            item {
                if (uiState.recentCommutes.isNotEmpty()) {
                    RecentCommutesSection(
                        recentCommutes = uiState.recentCommutes,
                        onCommuteClick = { commute ->
                            onNavigateToRouteDetails(commute.id)
                        }
                    )
                }
            }
            
            // Commute Insights
            item {
                CommuteInsightsCard(
                    insights = uiState.commuteInsights,
                    onInsightsClick = { /* TODO: Navigate to insights */ }
                )
            }
            
            // Traffic Summary
            item {
                TrafficSummaryCard(
                    trafficInfo = uiState.trafficSummary,
                    onTrafficClick = onNavigateToMapView
                )
            }
        }
    }
}

/**
 * Top bar for the home screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    onProfileClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onSettingsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "CommuteTimely",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            IconButton(onClick = onNotificationsClick) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications"
                )
            }
            IconButton(onClick = onSettingsClick) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
            IconButton(onClick = onProfileClick) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile"
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
 * Floating action button for creating new commutes
 */
@Composable
private fun HomeFloatingActionButton(
    onNewCommuteClick: () -> Unit
) {
    FloatingActionButton(
        onClick = onNewCommuteClick,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        shape = fabShape
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "New Commute"
        )
    }
}

/**
 * Quick actions section with navigation buttons
 */
@Composable
private fun QuickActionsSection(
    onNavigateToMapView: () -> Unit,
    onNavigateToWeatherSummary: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = cardShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                QuickActionButton(
                    icon = Icons.Default.Map,
                    label = "Map View",
                    onClick = onNavigateToMapView,
                    modifier = Modifier.weight(1f)
                )
                
                QuickActionButton(
                    icon = Icons.Default.WbSunny,
                    label = "Weather",
                    onClick = onNavigateToWeatherSummary,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

/**
 * Individual quick action button
 */
@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = buttonShape,
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
