package com.softserveacademy.travelin2026projectlab

import android.app.Application
import android.content.pm.PackageManager
import com.google.android.libraries.places.api.Places
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TravelinApp : Application() {
    override fun onCreate() {
        super.onCreate()

        val ai = try {
            packageManager.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
        } catch (e: Exception) {
            null
        }
        val apiKey = ai?.metaData?.getString("com.google.android.geo.API_KEY")

        val keyToUse = if (!apiKey.isNullOrBlank() && apiKey != "YOUR_API_KEY") {
            apiKey
        } else {
            "MISSING_API_KEY"
        }

        if (!Places.isInitialized()) {
            Places.initialize(this, keyToUse)
        }
    }
}
