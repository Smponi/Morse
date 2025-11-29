package com.mouse.mouse.data.model

import java.util.*

/**
 * Datenmodell für einen gespeicherten Morse-Übersetzungs-Eintrag
 * 
 * Wird verwendet um:
 * - History zu speichern
 * - Favoriten zu verwalten
 * - Wiederverwendbare Übersetzungen anzubieten
 */
data class MorseRecord(
    val id: String = UUID.randomUUID().toString(),
    val text: String,                               // Der originale Text
    val morse: String,                              // Die Morse-Übersetzung
    val timestamp: Long = System.currentTimeMillis(), // Wann wurde es erstellt
    val isFavorite: Boolean = false                 // Ist es ein Favorit?
)
