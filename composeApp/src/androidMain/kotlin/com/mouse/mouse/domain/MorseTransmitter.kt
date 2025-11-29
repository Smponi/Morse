package com.mouse.mouse.domain

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.mouse.mouse.data.model.OutputMode
import com.mouse.mouse.platform.getAndroidContext
import kotlinx.coroutines.delay

/**
 * Android Implementation des Morse Transmitters
 * 
 * Verantwortlich für:
 * - Abspielen von Morse-Code über Android Hardware (Vibration, Licht)
 * - Timing der Signale nach Morse-Standard
 * - Hardware-Zugriff (Vibrator, Camera Flash)
 */
actual class MorseTransmitter {
    
    // Morse-Timing Konstanten (basierend auf WPM - Words Per Minute)
    // Aktuell: ~10 WPM (120ms pro Zeiteinheit)
    private val timeUnit = 120L
    private val dotDuration = timeUnit              // Ein "Punkt" dauert 1 Zeiteinheit
    private val dashDuration = timeUnit * 3         // Ein "Strich" dauert 3 Zeiteinheiten
    private val symbolGap = timeUnit                // Pause zwischen Punkten/Strichen im selben Buchstaben
    private val letterGap = timeUnit * 3            // Pause zwischen Buchstaben
    
    private var isTransmitting = false
    
    // Callback für UI-Updates (z.B. Visualizer)
    actual var onSignalStateChanged: ((Boolean) -> Unit)? = null
    
    // Callback für Playback Progress (welches Zeichen wird gerade gespielt)
    actual var onPlaybackProgress: ((currentIndex: Int) -> Unit)? = null
    
    actual suspend fun transmit(morseCode: String, activeModes: List<OutputMode>): Boolean {
        val ctx = getAndroidContext() ?: return false
        if (isTransmitting || morseCode.isEmpty()) return false
        
        isTransmitting = true
        
        // Hardware initialisieren
        val vibrator = getVibrator(ctx)
        val cameraManager = ctx.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = try { cameraManager.cameraIdList.firstOrNull() } catch (e: Exception) { null }
        
        // Morse-Code Zeichen für Zeichen abspielen
        morseCode.forEachIndexed { index, symbol ->
            if (!isTransmitting) {
                // Vorzeitiger Abbruch
                signalOff(cameraManager, cameraId)
                onPlaybackProgress?.invoke(-1) // Reset
                return false
            }
            
            // UI benachrichtigen über aktuellen Progress
            onPlaybackProgress?.invoke(index)
            
            when (symbol) {
                '.' -> playDot(vibrator, cameraManager, cameraId, activeModes)
                '-' -> playDash(vibrator, cameraManager, cameraId, activeModes)
                ' ' -> delay(letterGap)  // Buchstabenpause
                '/' -> delay(letterGap)  // Wortpause (könnte auch länger sein)
            }
        }
        
        // Playback beendet
        onPlaybackProgress?.invoke(-1) // Reset
        
        isTransmitting = false
        signalOff(cameraManager, cameraId)
        return true
    }
    
    actual fun stop() {
        isTransmitting = false
    }
    
    actual fun isTransmitting(): Boolean = isTransmitting
    
    // ==================== Private Helper Methods ====================
    
    private suspend fun playDot(
        vibrator: Vibrator,
        camMan: CameraManager,
        camId: String?,
        activeModes: List<OutputMode>
    ) {
        activateSignal(vibrator, camMan, camId, activeModes, dotDuration)
        delay(dotDuration)
        signalOff(camMan, camId)
        delay(symbolGap)  // Pause bis zum nächsten Symbol
    }
    
    private suspend fun playDash(
        vibrator: Vibrator,
        camMan: CameraManager,
        camId: String?,
        activeModes: List<OutputMode>
    ) {
        activateSignal(vibrator, camMan, camId, activeModes, dashDuration)
        delay(dashDuration)
        signalOff(camMan, camId)
        delay(symbolGap)  // Pause bis zum nächsten Symbol
    }
    
    private fun activateSignal(
        vibrator: Vibrator,
        camMan: CameraManager,
        camId: String?,
        activeModes: List<OutputMode>,
        duration: Long
    ) {
        onSignalStateChanged?.invoke(true)  // UI benachrichtigen
        
        // Vibration aktivieren
        if (activeModes.contains(OutputMode.VIBRATION)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(
                    VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE)
                )
            } else {
                @Suppress("DEPRECATION")
                vibrator.vibrate(duration)
            }
        }
        
        // Flashlight aktivieren
        if (activeModes.contains(OutputMode.LIGHT) && camId != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    camMan.setTorchMode(camId, true)
                }
            } catch (e: Exception) {
                // Flashlight nicht verfügbar
            }
        }
    }
    
    private fun signalOff(camMan: CameraManager, camId: String?) {
        onSignalStateChanged?.invoke(false)  // UI benachrichtigen
        
        // Flashlight ausschalten
        if (camId != null) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    camMan.setTorchMode(camId, false)
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }
    
    private fun getVibrator(ctx: Context): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = ctx.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            ctx.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}
