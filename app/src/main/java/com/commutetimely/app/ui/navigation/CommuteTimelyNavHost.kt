package com.commutetimely.app.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.commutetimely.feature.home.presentation.HomeScreen
import com.commutetimely.feature.home.presentation.SimpleHomeScreen
import com.commutetimely.feature.commute.presentation.CommutePlannerScreen
import com.commutetimely.feature.map.presentation.MapViewScreen
import com.commutetimely.feature.map.presentation.SimpleMapViewScreen
import com.commutetimely.feature.weather.presentation.WeatherSummaryScreen
import com.commutetimely.feature.weather.presentation.SimpleWeatherScreen
import com.commutetimely.feature.route.presentation.RouteDetailsScreen

/**
 * Main navigation host for CommuteTimely application
 * 
 * This composable sets up the navigation graph with all the main screens
 * and defines the navigation transitions and animations between them.
 * 
 * @param navController The navigation controller for managing navigation
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@Composable
fun CommuteTimelyNavHost(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        enterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ) {
        
        // Home Screen - Main dashboard
        composable(
            route = Screen.Home.route
        ) {
            HomeScreen(
                onNavigateToCommutePlanner = {
                    navController.navigate(Screen.CommutePlanner.route)
                },
                onNavigateToMapView = {
                    navController.navigate(Screen.MapView.route)
                },
                onNavigateToWeatherSummary = {
                    navController.navigate(Screen.WeatherSummary.route)
                },
                onNavigateToRouteDetails = { routeId ->
                    navController.navigate(Screen.RouteDetails.createRoute(routeId))
                }
            )
        }
        
        // Commute Planner Screen
        composable(
            route = Screen.CommutePlanner.route
        ) {
            CommutePlannerScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMapView = { origin, destination ->
                    navController.navigate(Screen.MapView.createRoute(origin, destination))
                },
                onNavigateToRouteDetails = { routeId ->
                    navController.navigate(Screen.RouteDetails.createRoute(routeId))
                }
            )
        }
        
        // Map View Screen
        composable(
            route = Screen.MapView.route
        ) {
            SimpleMapViewScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToRouteDetails = { routeId ->
                    navController.navigate(Screen.RouteDetails.createRoute(routeId))
                }
            )
        }
        
        // Map View Screen with route parameters
        composable(
            route = Screen.MapView.routeWithParams,
            arguments = Screen.MapView.arguments
        ) { backStackEntry ->
            val origin = backStackEntry.arguments?.getString("origin") ?: ""
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            
            SimpleMapViewScreen(
                origin = origin,
                destination = destination,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToRouteDetails = { routeId ->
                    navController.navigate(Screen.RouteDetails.createRoute(routeId))
                }
            )
        }
        
        // Weather Summary Screen
        composable(
            route = Screen.WeatherSummary.route
        ) {
            SimpleWeatherScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        
        // Route Details Screen
        composable(
            route = Screen.RouteDetails.route,
            arguments = Screen.RouteDetails.arguments
        ) { backStackEntry ->
            val routeId = backStackEntry.arguments?.getString("routeId") ?: ""
            
            RouteDetailsScreen(
                routeId = routeId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToMapView = { origin, destination ->
                    navController.navigate(Screen.MapView.createRoute(origin, destination))
                }
            )
        }
    }
}
