package com.commutetimely.feature.map.presentation

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.commutetimely.core.domain.model.Location
import com.commutetimely.core.domain.model.RouteType
import com.commutetimely.core.ui.theme.*
import com.commutetimely.feature.map.presentation.components.*
import com.mapbox.maps.MapView
import com.mapbox.maps.Style
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationManager
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.delegates.MapPluginProviderDelegate
// import com.mapbox.maps.plugin.viewport.viewportPlugin

/**
 * Map View Screen for CommuteTimely
 * 
 * This screen provides an interactive map interface with:
 * - Mapbox map integration
 * - Route planning and display
 * - Location search and selection
 * - Real-time traffic information
 * - Weather overlays
 * 
 * @param origin Optional origin location for route planning
 * @param destination Optional destination location for route planning
 * @param onNavigateBack Navigation callback to go back
 * @param onNavigateToRouteDetails Navigation callback to route details
 * @param viewModel The map view model
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapViewScreen(
    origin: String = "",
    destination: String = "",
    onNavigateBack: () -> Unit,
    onNavigateToRouteDetails: (String) -> Unit,
    viewModel: MapViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    // Mapbox map view
    var mapView by remember { mutableStateOf<MapView?>(null) }
    var pointAnnotationManager by remember { mutableStateOf<PointAnnotationManager?>(null) }
    
    LaunchedEffect(origin, destination) {
        if (origin.isNotEmpty() && destination.isNotEmpty()) {
            viewModel.planRoute(origin, destination)
        }
    }
    
    LaunchedEffect(uiState.currentRoute) {
        uiState.currentRoute?.let { route ->
            // Update map with route information
            mapView?.let { map ->
                viewModel.displayRouteOnMap(map, route)
            }
        }
    }
    
    Scaffold(
        topBar = {
            MapTopBar(
                onNavigateBack = onNavigateBack,
                onSearchClick = { /* TODO: Show search */ },
                onLayersClick = { /* TODO: Show map layers */ }
            )
        },
        bottomBar = {
            MapBottomBar(
                routeInfo = uiState.currentRoute,
                onStartNavigation = { route ->
                    viewModel.startNavigation(route.id)
                    onNavigateToRouteDetails(route.id)
                },
                onRouteOptions = { /* TODO: Show route options */ }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Mapbox Map
            AndroidView(
                factory = { context ->
                    MapView(context).apply {
                        mapView = this
                        getMapboxMap().loadStyleUri(Style.MAPBOX_STREETS) { style ->
                            pointAnnotationManager = annotations.createPointAnnotationManager()
                        }
                    }
                },
                modifier = Modifier.fillMaxSize(),
                update = { map ->
                    // Update map when needed
                }
            )
            
            // Search Bar
            MapSearchBar(
                origin = uiState.origin,
                destination = uiState.destination,
                onOriginChange = { viewModel.updateOrigin(it) },
                onDestinationChange = { viewModel.updateDestination(it) },
                onPlanRoute = { origin, destination ->
                    viewModel.planRoute(origin, destination)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
            
            // Route Type Selector
            RouteTypeSelector(
                selectedRouteType = uiState.selectedRouteType,
                onRouteTypeSelected = { routeType ->
                    viewModel.updateRouteType(routeType)
                },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            )
            
            // Weather Overlay
            if (uiState.weatherInfo != null) {
                WeatherOverlay(
                    weatherInfo = uiState.weatherInfo!!,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(16.dp)
                )
            }
            
            // Traffic Information
            if (uiState.trafficInfo != null) {
                TrafficOverlay(
                    trafficInfo = uiState.trafficInfo!!,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(16.dp)
                )
            }
            
            // Loading Indicator
            if (uiState.isLoading) {
                MapLoadingIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            // Error Message
            uiState.error?.let { error ->
                MapErrorMessage(
                    error = error,
                    onRetry = { viewModel.retryLastOperation() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            }
        }
    }
}

/**
 * Top bar for the map screen
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapTopBar(
    onNavigateBack: () -> Unit,
    onSearchClick: () -> Unit,
    onLayersClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Map View",
                style = MaterialTheme.typography.titleLarge,
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
            IconButton(onClick = onSearchClick) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search"
                )
            }
            IconButton(onClick = onLayersClick) {
                Icon(
                    imageVector = Icons.Default.Layers,
                    contentDescription = "Map Layers"
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}

/**
 * Bottom bar showing route information and actions
 */
@Composable
private fun MapBottomBar(
    routeInfo: com.commutetimely.core.domain.model.Commute?,
    onStartNavigation: (com.commutetimely.core.domain.model.Commute) -> Unit,
    onRouteOptions: () -> Unit
) {
    AnimatedVisibility(
        visible = routeInfo != null,
        enter = slideInVertically() + fadeIn(),
        exit = slideOutVertically() + fadeOut()
    ) {
        routeInfo?.let { route ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = cardShape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Route Found",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "${route.estimatedDuration} min • ${String.format("%.1f", route.distance)} km",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        
                        IconButton(onClick = onRouteOptions) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "Route Options"
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Button(
                        onClick = { onStartNavigation(route) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = buttonShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Navigation,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Start Navigation")
                    }
                }
            }
        }
    }
}

/**
 * Search bar for origin and destination input
 */
@Composable
private fun MapSearchBar(
    origin: String,
    destination: String,
    onOriginChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onPlanRoute: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = searchBarShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            OutlinedTextField(
                value = origin,
                onValueChange = onOriginChange,
                label = { Text("Origin") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = inputShape,
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = destination,
                onValueChange = onDestinationChange,
                label = { Text("Destination") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = inputShape,
                singleLine = true
            )
            
            Spacer(modifier = Modifier.height(12.dp))
            
            Button(
                onClick = { onPlanRoute(origin, destination) },
                modifier = Modifier.fillMaxWidth(),
                shape = buttonShape,
                enabled = origin.isNotEmpty() && destination.isNotEmpty()
            ) {
                Icon(
                    imageVector = Icons.Default.Directions,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Plan Route")
            }
        }
    }
}

/**
 * Route type selector (driving, walking, cycling, transit)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RouteTypeSelector(
    selectedRouteType: RouteType,
    onRouteTypeSelected: (RouteType) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = chipShape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            RouteType.values().forEach { routeType ->
                FilterChip(
                    selected = selectedRouteType == routeType,
                    onClick = { onRouteTypeSelected(routeType) },
                    label = {
                        Text(
                            text = routeType.name.lowercase().capitalize(),
                            style = MaterialTheme.typography.labelSmall
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                    ),
                    shape = chipShape
                )
            }
        }
    }
}
