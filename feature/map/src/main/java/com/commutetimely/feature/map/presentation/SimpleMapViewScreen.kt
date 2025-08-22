package com.commutetimely.feature.map.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.commutetimely.core.domain.model.RouteType

/**
 * Simple Map View Screen - Functional implementation without complex Mapbox dependencies
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleMapViewScreen(
    origin: String = "",
    destination: String = "",
    onNavigateBack: () -> Unit,
    onNavigateToRouteDetails: (String) -> Unit = {}
) {
    var selectedRouteType by remember { mutableStateOf(RouteType.DRIVING) }
    var destinationInput by remember { mutableStateOf(destination) }
    var currentLocationText by remember { mutableStateOf("Current Location") }
    var isCalculatingRoute by remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Map View") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Location settings */ }) {
                        Icon(Icons.Default.LocationOn, contentDescription = "Current Location")
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
            // Map placeholder - simulated map view
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp)
                    .background(
                        color = Color(0xFF4CAF50),
                        shape = RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Map,
                        contentDescription = "Map",
                        modifier = Modifier.size(64.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Interactive Map View",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Route planning and navigation",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f),
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            // Route planning section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Route Planning",
                        style = MaterialTheme.typography.titleLarge
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Origin (current location)
                    OutlinedTextField(
                        value = currentLocationText,
                        onValueChange = { currentLocationText = it },
                        label = { Text("From") },
                        leadingIcon = {
                            Icon(Icons.Default.MyLocation, contentDescription = "Origin")
                        },
                        trailingIcon = {
                            IconButton(onClick = { 
                                currentLocationText = "Detecting location..."
                                // Simulate location detection
                            }) {
                                Icon(Icons.Default.Refresh, contentDescription = "Refresh Location")
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Destination
                    OutlinedTextField(
                        value = destinationInput,
                        onValueChange = { destinationInput = it },
                        label = { Text("To") },
                        leadingIcon = {
                            Icon(Icons.Default.LocationOn, contentDescription = "Destination")
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Route type selector
                    Text(
                        text = "Transportation Mode",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        RouteType.values().forEach { routeType ->
                            FilterChip(
                                selected = selectedRouteType == routeType,
                                onClick = { selectedRouteType = routeType },
                                label = {
                                    Text(getRouteTypeLabel(routeType))
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = getRouteTypeIcon(routeType),
                                        contentDescription = routeType.name,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    // Action buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = { 
                                if (destinationInput.isNotBlank()) {
                                    isCalculatingRoute = true
                                    // Simulate route calculation
                                }
                            },
                            enabled = destinationInput.isNotBlank() && !isCalculatingRoute,
                            modifier = Modifier.weight(1f)
                        ) {
                            if (isCalculatingRoute) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(Icons.Default.Directions, contentDescription = null)
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(if (isCalculatingRoute) "Calculating..." else "Get Directions")
                        }
                        
                        OutlinedButton(
                            onClick = { 
                                destinationInput = ""
                                isCalculatingRoute = false
                                currentLocationText = "Current Location"
                            },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.MyLocation, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Reset")
                        }
                    }
                }
            }
            
            // Quick actions
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = { /* TODO: Traffic info */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.Traffic, contentDescription = null)
                                Text("Traffic")
                            }
                        }
                        
                        OutlinedButton(
                            onClick = { /* TODO: Nearby places */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.NearMe, contentDescription = null)
                                Text("Nearby")
                            }
                        }
                        
                        OutlinedButton(
                            onClick = { /* TODO: Save location */ },
                            modifier = Modifier.weight(1f)
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(Icons.Default.BookmarkAdd, contentDescription = null)
                                Text("Save")
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun getRouteTypeLabel(routeType: RouteType): String {
    return when (routeType) {
        RouteType.DRIVING -> "Drive"
        RouteType.WALKING -> "Walk"
        RouteType.CYCLING -> "Bike"
        RouteType.TRANSIT -> "Transit"
    }
}

private fun getRouteTypeIcon(routeType: RouteType) = when (routeType) {
    RouteType.DRIVING -> Icons.Default.DirectionsCar
    RouteType.WALKING -> Icons.Default.DirectionsWalk
    RouteType.CYCLING -> Icons.Default.DirectionsBike
    RouteType.TRANSIT -> Icons.Default.DirectionsTransit
}
