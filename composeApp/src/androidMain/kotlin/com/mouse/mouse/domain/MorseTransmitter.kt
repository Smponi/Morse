package com.mouse.mouse.domain

import android.content.Context
import android.hardware.camera2.CameraManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import com.mouse.mouse.data.model.OutputMode
import kotlinx.coroutines.delay

/**
 * Hardware-Controller für Morse-Signal-Übertragung
 * 
 * Verantwortlich für:
 * - Abspielen von Morse-Code über Hardware (Vibration, Licht, Sound)
 * - Timing der Signale nach Morse-Standard
 * - Hardware-Zugriff (Vibrator, Camera Flash)
 */
class MorseTransmitter(private val context: Context) {
    
    // Morse-Timing Konstanten (basierend auf WPM - Words Per Minute)
    // Aktuell: ~10 WPM (120ms pro Zeiteinheit)
    private val timeUnit = 120L
    private val dotDuration = timeUnit              // Ein "Punkt" dauert 1 Zeiteinheit
    private val dashDuration = timeUnit * 3         // Ein "Strich" dauert 3 Zeiteinheiten
    private val symbolGap = timeUnit                // Pause zwischen Punkten/Strichen im selben Buchstaben
    private val letterGap = timeUnit * 3            // Pause zwischen Buchstaben
    
    private var isTransmitting = false
    
    // Callback für UI-Updates (z.B. Visualizer)
    var onSignalStateChanged: ((Boolean) -> Unit)? = null
    
    /**
     * Überträgt einen Morse-Code String
     * 
     * @param morseCode Der zu übertragende Morse-Code (z.B. "... --- ...")
     * @param activeModes Welche Output-Methoden aktiv sind
     * @return true wenn erfolgreich übertragen, false wenn abgebrochen
     */
    suspend fun transmit(morseCode: String, activeModes: List<OutputMode>): Boolean {
        if (isTransmitting || morseCode.isEmpty()) return false
        
        isTransmitting = true
        
        // Hardware initialisieren
        val vibrator = getVibrator()
        val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraId = try { cameraManager.cameraIdList.firstOrNull() } catch (e: Exception) { null }
        
        // Morse-Code Zeichen für Zeichen abspielen
        morseCode.forEach { symbol ->
            if (!isTransmitting) {
                // Vorzeitiger Abbruch
                signalOff(cameraManager, cameraId)
                return false
            }
            
            when (symbol) {
                '.' -> playDot(vibrator, cameraManager, cameraId, activeModes)
                '-' -> playDash(vibrator, cameraManager, cameraId, activeModes)
                ' ' -> delay(letterGap)  // Buchstabenpause
                '/' -> delay(letterGap)  // Wortpause (könnte auch länger sein)
            }
        }
        
        isTransmitting = false
        signalOff(cameraManager, cameraId)
        return true
    }
    
    /**
     * Stoppt die aktuelle Übertragung
     */
    fun stop() {
        isTransmitting = false
    }
    
    /**
     * Gibt zurück ob gerade eine Übertragung läuft
     */
    fun isTransmitting(): Boolean = isTransmitting
    
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
    
    private fun getVibrator(): Vibrator {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            vibratorManager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }
    }
}
