package com.softserveacademy.travelin2026projectlab

import android.app.Application
import android.content.pm.PackageManager
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TravelinApp : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize Google Places SDK
        val ai = packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        val apiKey = ai.metaData.getString("com.google.android.geo.API_KEY")
        
        if (apiKey != null && apiKey != "YOUR_API_KEY") {
            if (!Places.isInitialized()) {
                Places.initialize(this, apiKey)
            }
        }
    }
}
