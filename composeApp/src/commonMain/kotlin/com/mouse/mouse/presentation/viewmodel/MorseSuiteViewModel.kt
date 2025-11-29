package com.mouse.mouse.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.mouse.mouse.data.model.InputMode
import com.mouse.mouse.data.model.MorseRecord
import com.mouse.mouse.data.model.OutputMode
import com.mouse.mouse.data.repository.MorseHistoryRepository
import com.mouse.mouse.domain.MorseDictionary
import com.mouse.mouse.domain.MorseTransmitter

/**
 * Haupt-ViewModel der Morse-App
 * 
 * Verantwortlich für:
 * - State Management (Input, Output, Modi)
 * - Koordination zwischen UI und Business Logic
 * - History Management
 * - Signal-Übertragung
 */
class MorseSuiteViewModel : ViewModel() {
    
    // ==================== Dependencies ====================
    private val historyRepository = MorseHistoryRepository()
    private var transmitter: MorseTransmitter? = null
    
    // ==================== UI State ====================
    
    // Input Mode (TEXT oder MORSE)
    var inputMode by mutableStateOf(InputMode.TEXT)
        private set
    
    // Text Input (wenn im TEXT Modus)
    var textInput by mutableStateOf("")
        private set
    
    // Morse Input (wenn im MORSE Modus)
    var morseInput by mutableStateOf("")
        private set
    
    // Aktive Output-Methoden (Vibration, Light, Sound)
    val activeModes = mutableStateListOf(OutputMode.VIBRATION, OutputMode.LIGHT)
    
    // Playback State
    var isPlaying by mutableStateOf(false)
        private set
    
    var isSignalActive by mutableStateOf(false)
        private set
    
    // Playback Progress: Welcher Character wird gerade gespielt (-1 = nicht spielend)
    var playbackIndex by mutableStateOf(-1)
        private set
    
    // ==================== Computed Properties ====================
    
    /**
     * Text der im oberen Display angezeigt wird
     * TEXT Modus: Der eingegebene Text
     * MORSE Modus: Der eingegebene Morse-Code
     */
    val displayTop: String
        get() = if (inputMode == InputMode.TEXT) textInput else morseInput
    
    /**
     * Text der im unteren Display angezeigt wird (Übersetzung)
     * TEXT Modus: Morse-Übersetzung des Texts
     * MORSE Modus: Text-Übersetzung des Morse-Codes
     */
    val displayBottom: String
        get() = if (inputMode == InputMode.TEXT) {
            MorseDictionary.textToMorse(textInput)
        } else {
            MorseDictionary.morseToText(morseInput)
        }
    
    /**
     * Der Morse-Code der übertragen werden soll
     */
    val morseToTransmit: String
        get() = if (inputMode == InputMode.TEXT) {
            MorseDictionary.textToMorse(textInput)
        } else {
            morseInput
        }
    
    // ==================== History ====================
    
    val history: List<MorseRecord>
        get() = historyRepository.getHistory()
    
    // ==================== Actions ====================
    
    /**
     * Wird aufgerufen wenn der User Text eingibt (TEXT Modus)
     */
    fun onTextInputChange(newText: String) {
        textInput = newText
        // Optional: Morse-Input synchronisieren
        morseInput = MorseDictionary.textToMorse(newText)
    }
    
    /**
     * Wird aufgerufen wenn der User Morse-Code eingibt (MORSE Modus)
     */
    fun onMorseInputChange(newMorse: String) {
        morseInput = newMorse
        // Optional: Text-Input synchronisieren
        textInput = MorseDictionary.morseToText(newMorse)
    }
    
    /**
     * Fügt ein Morse-Symbol hinzu (. oder -)
     * Wird vom Custom Keyboard aufgerufen
     */
    fun appendMorse(symbol: String) {
        morseInput += symbol
        textInput = MorseDictionary.morseToText(morseInput)
    }
    
    /**
     * Fügt ein Leerzeichen hinzu (trennt Buchstaben)
     * Wird vom Custom Keyboard aufgerufen
     */
    fun appendLetterSpace() {
        if (!morseInput.endsWith(" ")) {
            morseInput += " "
            textInput = MorseDictionary.morseToText(morseInput)
        }
    }
    
    /**
     * Fügt ein Wort-Leerzeichen hinzu (/)
     * Wird vom Custom Keyboard aufgerufen
     */
    fun appendWordSpace() {
        if (!morseInput.endsWith(" / ")) {
            // Stelle sicher dass vorher ein Letter Space ist
            if (!morseInput.endsWith(" ")) {
                morseInput += " "
            }
            morseInput += "/ "
            textInput = MorseDictionary.morseToText(morseInput)
        }
    }
    
    /**
     * Löscht das letzte Zeichen
     * Wird vom Custom Keyboard aufgerufen
     */
    fun deleteLastMorse() {
        if (morseInput.isNotEmpty()) {
            morseInput = morseInput.dropLast(1)
            textInput = MorseDictionary.morseToText(morseInput)
        }
    }
    
    /**
     * Wechselt zwischen TEXT und MORSE Modus
     */
    fun toggleInputMode() {
        inputMode = if (inputMode == InputMode.TEXT) InputMode.MORSE else InputMode.TEXT
        // Inputs zurücksetzen für sauberen State
        textInput = ""
        morseInput = ""
    }
    
    /**
     * Aktiviert/Deaktiviert eine Output-Methode
     */
    fun toggleMode(mode: OutputMode) {
        if (activeModes.contains(mode)) {
            activeModes.remove(mode)
        } else {
            activeModes.add(mode)
        }
    }
    
    /**
     * Startet die Morse-Signal-Übertragung
     */
    suspend fun transmitSignal() {
        val morse = morseToTransmit
        if (morse.isEmpty()) return
        
        // Zur History hinzufügen
        addToHistory()
        
        // Transmitter initialisieren (lazy) - beim ersten Aufruf
        if (transmitter == null) {
            transmitter = createTransmitter()
        }
        
        isPlaying = true
        transmitter?.transmit(morse, activeModes.toList())
        isPlaying = false
        isSignalActive = false
        playbackIndex = -1 // Reset nach Playback
    }
    
    /**
     * Stoppt die laufende Übertragung
     */
    fun stopTransmission() {
        transmitter?.stop()
        isPlaying = false
        isSignalActive = false
        playbackIndex = -1 // Reset
    }
    
    /**
     * Fügt die aktuelle Übersetzung zur History hinzu
     */
    private fun addToHistory() {
        val text = if (inputMode == InputMode.TEXT) textInput else displayBottom
        val morse = morseToTransmit
        historyRepository.addRecord(text, morse)
    }
    
    /**
     * Toggled Favoriten-Status eines History-Records
     */
    fun toggleFavorite(recordId: String) {
        historyRepository.toggleFavorite(recordId)
    }
    
    /**
     * Lädt einen Text aus der History zurück ins Input-Feld
     */
    fun loadFromHistory(text: String) {
        if (inputMode == InputMode.TEXT) {
            textInput = text
        } else {
            morseInput = MorseDictionary.textToMorse(text)
            textInput = text
        }
    }
    
    // ==================== Platform-specific Initialization ====================
    
    /**
     * Erstellt einen platform-spezifischen Transmitter
     * Muss von platform-spezifischem Code aufgerufen werden
     */
    private fun createTransmitter(): MorseTransmitter {
        return MorseTransmitter().apply {
            onSignalStateChanged = { active ->
                isSignalActive = active
            }
            onPlaybackProgress = { index ->
                playbackIndex = index
            }
        }
    }
}
