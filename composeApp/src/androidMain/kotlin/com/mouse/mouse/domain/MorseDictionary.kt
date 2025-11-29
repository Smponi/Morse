package com.mouse.mouse.domain

/**
 * Internationales Morse-Code Dictionary
 * 
 * Verantwortlich für:
 * - Text → Morse Übersetzung
 * - Morse → Text Übersetzung
 * 
 * Verwendet das Internationale Morse Alphabet (ITU Standard)
 */
object MorseDictionary {
    
    // Mapping: Buchstabe/Zahl → Morse-Code
    private val charToMorse = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
        'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
        'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
        'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
        'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
        'Z' to "--..",
        '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--",
        '4' to "....-", '5' to ".....", '6' to "-....", '7' to "--...",
        '8' to "---..", '9' to "----.",
        ' ' to "/"  // Leerzeichen wird als Slash dargestellt
    )
    
    // Reverse Mapping: Morse-Code → Buchstabe/Zahl
    private val morseToChar = charToMorse.entries.associate { (k, v) -> v to k }
    
    /**
     * Übersetzt Text in Morse-Code
     * 
     * @param text Der zu übersetzende Text (case-insensitive)
     * @return Morse-Code String (Buchstaben durch Leerzeichen getrennt)
     * 
     * Beispiel: "SOS" → "... --- ..."
     */
    fun textToMorse(text: String): String {
        return text.uppercase()
            .map { char -> charToMorse[char] ?: "" }
            .filter { it.isNotEmpty() }  // Unbekannte Zeichen ignorieren
            .joinToString(" ")
    }
    
    /**
     * Übersetzt Morse-Code in Text
     * 
     * @param morse Morse-Code String (Buchstaben durch Leerzeichen getrennt)
     * @return Der dekodierte Text
     * 
     * Beispiel: "... --- ..." → "SOS"
     * Beispiel: ".... . .-.. .-.. --- / .-- --- .-. .-.. -.." → "HELLO WORLD"
     */
    fun morseToText(morse: String): String {
        return morse.trim()
            .split(" ")  // Trenne bei Leerzeichen (= Buchstabentrenner)
            .map { code ->
                when {
                    code == "/" -> " "  // Slash = Worttrenner
                    code.isEmpty() -> ""
                    else -> morseToChar[code]?.toString() ?: "?"  // Unbekannt = ?
                }
            }
            .joinToString("")
    }
    
    /**
     * Prüft ob ein String gültiger Morse-Code ist
     * @param morse Der zu prüfende String
     * @return true wenn nur ., -, Leerzeichen und / enthalten sind
     */
    fun isValidMorse(morse: String): Boolean {
        return morse.all { it in listOf('.', '-', ' ', '/') }
    }
}
