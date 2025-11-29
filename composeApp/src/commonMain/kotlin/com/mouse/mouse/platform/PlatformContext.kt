package com.mouse.mouse.platform

/**
 * Initialisiert platform-spezifische Hardware
 * Wird von der platform-spezifischen MainActivity/Main aufgerufen
 */
expect fun initializeHardware(context: Any)
