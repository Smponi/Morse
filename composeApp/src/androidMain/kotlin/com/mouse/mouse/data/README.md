# Data Layer

Dieser Layer ist verantwortlich für alle Daten-bezogenen Operationen.

## 📂 Struktur

### `model/`
Enthält die Datenmodelle der App:

- **MorseRecord**: Repräsentiert einen gespeicherten Morse-Übersetzungs-Eintrag
- **OutputMode**: Enum für Ausgabe-Methoden (Vibration, Light, Sound)
- **InputMode**: Enum für Eingabe-Modi (Text, Morse)

### `repository/`
Verwaltet den Zugriff auf Daten:

- **MorseHistoryRepository**: 
  - Speichert und lädt History-Einträge
  - Verwaltet Favoriten
  - Aktuell: In-Memory Storage
  - Zukunft: Room Database

## 🔧 Verwendung

```kotlin
// Repository erstellen
val repository = MorseHistoryRepository()

// Neuen Record hinzufügen
repository.addRecord(text = "SOS", morse = "... --- ...")

// History abrufen
val history: List<MorseRecord> = repository.getHistory()

// Favorit togglen
repository.toggleFavorite(recordId = "123")
```

## 🔮 Zukunft

- [ ] Room Database Integration
- [ ] DataStore für Settings
- [ ] Cloud Sync für History
