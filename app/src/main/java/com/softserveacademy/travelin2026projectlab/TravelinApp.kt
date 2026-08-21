package com.softserveacademy.travelin2026projectlab

import android.app.Application
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TravelinApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val apiKey = BuildConfig.MAPS_API_KEY

        val keyToUse = if (apiKey.isNotBlank() && apiKey != "YOUR_API_KEY") {
            apiKey
        } else {
            "MISSING_API_KEY"
        }

        if (!Places.isInitialized()) {
            Places.initialize(this, keyToUse)
        }
    }
}
