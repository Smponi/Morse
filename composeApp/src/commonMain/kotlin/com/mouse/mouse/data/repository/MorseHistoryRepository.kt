package com.mouse.mouse.data.repository

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.mouse.mouse.data.model.MorseRecord

/**
 * Repository für die Verwaltung der Morse-Übersetzungs-History
 * 
 * Responsibilities:
 * - Speichern neuer Records
 * - Abrufen der History
 * - Favoriten-Management
 * - (Zukünftig: Persistierung in Datenbank)
 */
class MorseHistoryRepository {
    
    // In-Memory Storage (später durch Room Database ersetzen)
    private val _history: SnapshotStateList<MorseRecord> = mutableStateListOf()
    
    /**
     * Gibt die komplette History zurück (neueste zuerst)
     */
    fun getHistory(): List<MorseRecord> = _history.toList()
    
    /**
     * Fügt einen neuen Record zur History hinzu
     * @param text Der originale Text
     * @param morse Die Morse-Übersetzung
     */
    fun addRecord(text: String, morse: String) {
        if (text.isBlank()) return
        
        val record = MorseRecord(
            text = text,
            morse = morse
        )
        
        // Am Anfang einfügen (neueste zuerst)
        _history.add(0, record)
    }
    
    /**
     * Toggled den Favoriten-Status eines Records
     * @param recordId Die ID des zu ändernden Records
     */
    fun toggleFavorite(recordId: String) {
        val index = _history.indexOfFirst { it.id == recordId }
        if (index != -1) {
            val record = _history[index]
            _history[index] = record.copy(isFavorite = !record.isFavorite)
        }
    }
    
    /**
     * Gibt nur die als Favorit markierten Records zurück
     */
    fun getFavorites(): List<MorseRecord> {
        return _history.filter { it.isFavorite }
    }
    
    /**
     * Löscht einen Record aus der History
     * @param recordId Die ID des zu löschenden Records
     */
    fun deleteRecord(recordId: String) {
        _history.removeIf { it.id == recordId }
    }
    
    /**
     * Löscht die komplette History
     */
    fun clearHistory() {
        _history.clear()
    }
}
