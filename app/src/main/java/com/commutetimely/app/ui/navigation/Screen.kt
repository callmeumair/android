package com.commutetimely.app.ui.navigation

import androidx.navigation.NavType
import androidx.navigation.navArgument

/**
 * Screen definitions for CommuteTimely navigation
 * 
 * This sealed class defines all the screens in the application along with
 * their routes, arguments, and navigation parameters.
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
sealed class Screen(val route: String) {
    
    /**
     * Home screen - Main dashboard
     */
    object Home : Screen("home")
    
    /**
     * Commute planner screen
     */
    object CommutePlanner : Screen("commute_planner")
    
    /**
     * Map view screen
     */
    object MapView : Screen("map_view") {
        const val routeWithParams = "map_view/{origin}/{destination}"
        
        val arguments = listOf(
            navArgument("origin") {
                type = NavType.StringType
                nullable = true
                defaultValue = ""
            },
            navArgument("destination") {
                type = NavType.StringType
                nullable = true
                defaultValue = ""
            }
        )
        
        fun createRoute(origin: String, destination: String): String {
            return "map_view/$origin/$destination"
        }
    }
    
    /**
     * Weather summary screen
     */
    object WeatherSummary : Screen("weather_summary")
    
    /**
     * Route details screen
     */
    object RouteDetails : Screen("route_details/{routeId}") {
        val arguments = listOf(
            navArgument("routeId") {
                type = NavType.StringType
            }
        )
        
        fun createRoute(routeId: String): String {
            return "route_details/$routeId"
        }
    }
    
    /**
     * Settings screen
     */
    object Settings : Screen("settings")
    
    /**
     * Profile screen
     */
    object Profile : Screen("profile")
    
    /**
     * Notifications screen
     */
    object Notifications : Screen("notifications")
    
    /**
     * Help and support screen
     */
    object HelpSupport : Screen("help_support")
    
    /**
     * About screen
     */
    object About : Screen("about")
    
    /**
     * Privacy policy screen
     */
    object PrivacyPolicy : Screen("privacy_policy")
    
    /**
     * Terms of service screen
     */
    object TermsOfService : Screen("terms_of_service")
    
    /**
     * Feedback screen
     */
    object Feedback : Screen("feedback")
    
    /**
     * Route history screen
     */
    object RouteHistory : Screen("route_history")
    
    /**
     * Favorites screen
     */
    object Favorites : Screen("favorites")
    
    /**
     * Search results screen
     */
    object SearchResults : Screen("search_results") {
        const val routeWithParams = "search_results/{query}"
        
        val arguments = listOf(
            navArgument("query") {
                type = NavType.StringType
            }
        )
        
        fun createRoute(query: String): String {
            return "search_results/$query"
        }
    }
    
    /**
     * Location picker screen
     */
    object LocationPicker : Screen("location_picker") {
        const val routeWithParams = "location_picker/{type}"
        
        val arguments = listOf(
            navArgument("type") {
                type = NavType.StringType
                defaultValue = "origin"
            }
        )
        
        fun createRoute(type: String): String {
            return "location_picker/$type"
        }
    }
    
    /**
     * Route comparison screen
     */
    object RouteComparison : Screen("route_comparison") {
        const val routeWithParams = "route_comparison/{routeIds}"
        
        val arguments = listOf(
            navArgument("routeIds") {
                type = NavType.StringType
            }
        )
        
        fun createRoute(routeIds: String): String {
            return "route_comparison/$routeIds"
        }
    }
    
    /**
     * Weather alerts screen
     */
    object WeatherAlerts : Screen("weather_alerts")
    
    /**
     * Traffic alerts screen
     */
    object TrafficAlerts : Screen("traffic_alerts")
    
    /**
     * Commute insights screen
     */
    object CommuteInsights : Screen("commute_insights")
    
    /**
     * Energy efficiency screen
     */
    object EnergyEfficiency : Screen("energy_efficiency")
    
    /**
     * Carbon footprint screen
     */
    object CarbonFootprint : Screen("carbon_footprint")
    
    /**
     * Public transit info screen
     */
    object PublicTransitInfo : Screen("public_transit_info")
    
    /**
     * Bike routes screen
     */
    object BikeRoutes : Screen("bike_routes")
    
    /**
     * Walking routes screen
     */
    object WalkingRoutes : Screen("walking_routes")
    
    /**
     * Driving routes screen
     */
    object DrivingRoutes : Screen("driving_routes")
}
