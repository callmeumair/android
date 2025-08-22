package com.commutetimely.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Main Application class for CommuteTimely
 * 
 * This class initializes the Hilt dependency injection framework
 * and serves as the entry point for the application.
 * 
 * @author CommuteTimely Team
 * @since 1.0.0
 */
@HiltAndroidApp
class CommuteTimelyApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Initialize any application-level configurations here
        // Such as crash reporting, analytics, etc.
    }
}
