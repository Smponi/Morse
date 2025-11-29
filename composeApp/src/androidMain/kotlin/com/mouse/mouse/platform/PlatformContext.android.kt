package com.mouse.mouse.platform

import android.content.Context

private var androidContext: Context? = null

/**
 * Speichert den Android Context für spätere Verwendung
 */
actual fun initializeHardware(context: Any) {
    androidContext = context as? Context
}

/**
 * Gibt den gespeicherten Android Context zurück
 */
fun getAndroidContext(): Context? = androidContext
