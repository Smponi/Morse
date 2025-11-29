# Morse App - Code Struktur

Diese Datei erklärt die Architektur und Struktur der Morse-App.

## 📁 Package-Struktur

```
com.mouse.mouse/
├── data/               # Daten-Layer (Models, Repositories)
├── domain/             # Business-Logic (Use Cases, Services)
├── presentation/       # UI-Layer (ViewModels, Screens, Components)
└── ui/                 # Design System (Theme, Colors, Dimensions)
```

## 🏗️ Architektur

Die App folgt einer **Clean Architecture** Struktur:

### Data Layer (`data/`)
Verantwortlich für Datenhaltung und -verwaltung
- **models/**: Datenmodelle (MorseRecord, OutputMode, etc.)
- **repository/**: Datenzugriff und -verwaltung (History, Favoriten)

### Domain Layer (`domain/`)
Enthält die Business-Logic ohne Android-Dependencies
- **MorseDictionary**: Übersetzung Text ↔ Morse
- **MorseTransmitter**: Hardware-Steuerung für Signal-Übertragung

### Presentation Layer (`presentation/`)
UI und Benutzerinteraktion
- **viewmodel/**: State Management (MorseSuiteViewModel)
- **screens/**: Vollständige Screens (Transmitter, History)
- **components/**: Wiederverwendbare UI-Komponenten

### UI Layer (`ui/`)
Design System und Theming
- **theme/**: Farben, Typography, Design Tokens

## 🔄 Datenfluss

```
User Input
    ↓
Screen (Composable)
    ↓
ViewModel
    ↓
Domain (Business Logic)
    ↓
Data (Repository)
```

## 🎨 Design Tokens

Alle Größen, Abstände und Radien sind in `ui/theme/Dimensions.kt` definiert.
**Verwende IMMER diese Tokens statt hardcoded Werte!**

Beispiel:
```kotlin
// ❌ Falsch
Modifier.padding(16.dp)

// ✅ Richtig
Modifier.padding(AppDimensions.Spacing.medium)
```

## 📝 Code-Konventionen

1. **Kommentare**: Jede Klasse hat einen Header-Kommentar der erklärt was sie tut
2. **Funktionen**: Öffentliche Funktionen haben KDoc-Kommentare
3. **Design Tokens**: Verwende `AppDimensions` für alle Größen
4. **State**: State wird nur im ViewModel gehalten, nicht in Composables

## 🚀 Nächste Schritte

1. [ ] Room Database für persistente History
2. [ ] Sound-Output implementieren
3. [ ] Unit Tests für Domain Layer
4. [ ] UI Tests für kritische Flows
