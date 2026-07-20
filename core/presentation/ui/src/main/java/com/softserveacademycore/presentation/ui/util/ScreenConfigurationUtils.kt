package com.softserveacademycore.presentation.ui.util

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalContext

/**
 * Locks the screen orientation to the provided [orientation].
 */

@Composable
fun LockScreenOrientation(orientation: Int) {
    val context = LocalContext.current
    DisposableEffect(orientation) {
        val activity = context as? Activity ?: return@DisposableEffect onDispose {}
        val originalOrientation = activity.requestedOrientation
        activity.requestedOrientation = orientation
        onDispose {
            // when we leave the screen, restore the original orientation
            activity.requestedOrientation = originalOrientation
        }
    }
}