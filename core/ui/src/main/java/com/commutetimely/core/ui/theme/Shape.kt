package com.commutetimely.core.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * CommuteTimely Shape System
 * 
 * This file defines the complete shape system following Material 3 principles
 * with custom shapes optimized for commute applications, including:
 * - Consistent corner radius values
 * - Specialized shapes for different UI components
 * - Accessibility considerations for touch targets
 * - Weather and map-specific shape definitions
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */

// Set of Material 3 shapes to start with
val Shapes = Shapes(
    extraSmall = RoundedCornerShape(4.dp),
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(12.dp),
    large = RoundedCornerShape(16.dp),
    extraLarge = RoundedCornerShape(28.dp),
)

/**
 * Custom Shape Extensions for CommuteTimely
 * 
 * These shapes provide specialized corner radius values for specific use cases
 * in the commute application.
 */

// Card shapes
val cardShape = RoundedCornerShape(16.dp)
val cardShapeSmall = RoundedCornerShape(12.dp)
val cardShapeLarge = RoundedCornerShape(24.dp)

// Button shapes
val buttonShape = RoundedCornerShape(12.dp)
val buttonShapeSmall = RoundedCornerShape(8.dp)
val buttonShapeLarge = RoundedCornerShape(16.dp)
val fabShape = RoundedCornerShape(16.dp)

// Input field shapes
val inputShape = RoundedCornerShape(12.dp)
val searchBarShape = RoundedCornerShape(24.dp)
val chipShape = RoundedCornerShape(16.dp)

// Weather widget shapes
val weatherCardShape = RoundedCornerShape(20.dp)
val weatherIconShape = RoundedCornerShape(16.dp)
val temperatureDisplayShape = RoundedCornerShape(12.dp)

// Traffic indicator shapes
val trafficIndicatorShape = RoundedCornerShape(8.dp)
val trafficBarShape = RoundedCornerShape(4.dp)
val statusBadgeShape = RoundedCornerShape(12.dp)

// Route and map shapes
val routeCardShape = RoundedCornerShape(16.dp)
val mapOverlayShape = RoundedCornerShape(12.dp)
val locationPinShape = RoundedCornerShape(50)

// Navigation shapes
val bottomSheetShape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
val modalShape = RoundedCornerShape(24.dp)
val drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)

// List item shapes
val listItemShape = RoundedCornerShape(12.dp)
val listItemShapeSmall = RoundedCornerShape(8.dp)
val listItemShapeLarge = RoundedCornerShape(16.dp)

// Progress and loading shapes
val progressBarShape = RoundedCornerShape(4.dp)
val loadingSkeletonShape = RoundedCornerShape(8.dp)
val shimmerShape = RoundedCornerShape(12.dp)

// Notification and alert shapes
val notificationShape = RoundedCornerShape(16.dp)
val alertShape = RoundedCornerShape(12.dp)
val toastShape = RoundedCornerShape(24.dp)

// Accessibility shapes
val accessibilityButtonShape = RoundedCornerShape(8.dp)
val accessibilityCardShape = RoundedCornerShape(12.dp)
val accessibilityInputShape = RoundedCornerShape(8.dp)
