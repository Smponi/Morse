package com.mouse.mouse.data.model

/**
 * Definiert den Eingabemodus der App
 * 
 * TEXT: Benutzer gibt normalen Text ein → wird zu Morse übersetzt
 * MORSE: Benutzer gibt Morse-Code ein (via Custom Keyboard) → wird zu Text übersetzt
 */
enum class InputMode {
    TEXT,
    MORSE
}
