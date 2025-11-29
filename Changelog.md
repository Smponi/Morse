# Changelog

## 16. November 2025 - 15:30 Uhr - Initial UI/UX Implementation (Build erfolgreich ✓)

### Implemented
- **Design System** - Vollständiges Material 3 Theme System mit Custom Tokens
  - Color Tokens (Primary, Secondary, Surface, Channel-spezifische Farben)
  - Typography Tokens (Standard + Morse-spezifische Monospace Styles)
  - Spacing, Radius, Elevation, Size Tokens
  - Motion & Timing Tokens (inkl. Morse-Unit-Defaults)

- **OnBoarding Flow** - 3-Screen Onboarding Experience
  - Screen 1: Intro mit App-Beschreibung
  - Screen 2: Morse Demo Visualisierung (HI → .... ..)
  - Screen 3: Channel Selection mit Safety-Hinweis
  - OnboardingViewModel mit Channel-State Management

- **Home Screen** - Haupt-Translate Screen
  - Segmented Control für Moduswahl (Text→Morse / Morse→Text)
  - TextInputCard für Text-Eingabe
  - MorseInputCard mit Tap-Button für Morse-Eingabe
  - OutputCard mit Copy/Share Actions
  - PlaybackPanel mit Play/Stop Button und Channel Chips
  - HomeViewModel mit State Management

- **UI Components**
  - ModeSegmentedControl - Animierter Mode-Switcher
  - TextInputCard - Multi-line Text Input mit Character Count
  - MorseInputCard - Tap-Button mit Press-Duration Detection
  - OutputCard - Flexible Output Display für Text/Morse
  - PlaybackPanel - Sticky Bottom Panel mit Channels
  - ChannelChip - Animierte Filter Chips für Flash/Vibration/Sound/Visual

- **Navigation & Screens**
  - Navigation Routes (Onboarding, Home, History, Settings)
  - HistoryScreen - Placeholder mit Empty State
  - SettingsScreen - Vollständige Settings UI mit Sections für:
    - Ausgabekanäle (Flash, Vibration, Sound, Visual)
    - Morse-Geschwindigkeit Slider
    - Safety Settings (Flacker-reduzierter Modus)
    - Datenschutz (Verlauf speichern/löschen)

- **Core Domain Models**
  - MorseMode Enum (TEXT_TO_MORSE, MORSE_TO_TEXT)
  - OutputChannel Enum (FLASH, VIBRATION, SOUND, VISUAL)
  - PlaybackState Enum (IDLE, PREPARING, PLAYING, ERROR)
  - ChannelState Data Class

- **ViewModels mit TODO-Markierungen**
  - OnboardingViewModel - Channel Selection & Onboarding Complete
  - HomeViewModel - Input Management, Mode Switching, Playback Control
  - Alle Business-Logik-Calls mit TODOs für Repository-Anbindung

### TODOs für Business-Logik (vom Entwickler zu implementieren)
- Text→Morse Konvertierung
- Morse→Text Dekodierung
- Tap-Duration zu Dot/Dash Interpretation
- Playback Engine für multimodale Ausgabe
- Preferences Management (Onboarding Flag, Settings)
- History Persistence
- Clipboard & Share Funktionalität
- Permission Handling (Flash, Vibration)
- Audio Focus Management

### Architektur
- Package-Struktur:
  - `core.domain.model` - Domain Models
  - `presentation.onboarding` - OnBoarding Feature
  - `presentation.home` - Home/Translate Feature
  - `presentation.history` - History Feature
  - `presentation.settings` - Settings Feature
  - `ui.theme` - Design System
  - `ui.components` - Wiederverwendbare UI Components
  - `navigation` - Navigation Routes

### Design-Spezifikation
- Basiert auf docs/UX/design-spec.md und design-spec-flows.md
- Material 3 / Material You Compliance
- Accessibility: Focus auf Safety, Screenreader Support, Skalierbare Schriften
- Multimodale UX: Flash, Vibration, Sound, Visual mit klarer Kontrollierbarkeit

### Build-Status
- ✓ Kompiliert erfolgreich (Android Debug)
- ✓ Material Icons Extended integriert (für Android & iOS)
- ✓ Alle Imports korrekt

## 16. November 2025 - 16:00 Uhr - UI Refinements & Material Icons

### Änderungen
- **Material Icons Integration**
  - Material Icons Extended hinzugefügt (compose.materialIconsExtended)
  - Alle Icons durch native Material Icons ersetzt
  - Icons: History, Settings, ArrowBack, ContentCopy, Share, PlayArrow, Stop

- **HomeScreen Design Optimierungen**
  - Spacing von lg auf md reduziert → kompakteres Layout
  - Card Padding von cardPadding auf lg vereinheitlicht
  - Textfeld minLines von 3 auf 2 reduziert
  - Spacer-Höhen minimiert (xl → md, sm → xs)
  - Weniger leere Flächen, dichteres UI

- **Komponenten Verbesserungen**
  - TextInputCard: kompakteres Padding, weniger Zeilen
  - MorseInputCard: reduzierte Abstände zwischen Elementen
  - OutputCard: einheitliches Spacing
  - PlaybackPanel: Material Icons für Play/Stop Buttons

## 29/11/2024 - 20:45 - Major Refactoring: Clean Architecture

### 🏗️ Architektur-Überarbeitung
- **Komplette Code-Reorganisation** in Clean Architecture Pattern
- Code aufgeteilt in 21 separate, gut dokumentierte Dateien
- Jede Komponente in eigener Datei für bessere Wartbarkeit

### 📁 Neue Package-Struktur
```
com.mouse.mouse/
├── data/
│   ├── model/ (MorseRecord, OutputMode, InputMode)
│   └── repository/ (MorseHistoryRepository)
├── domain/ (MorseDictionary, MorseTransmitter)
├── presentation/
│   ├── viewmodel/ (MorseSuiteViewModel)
│   ├── screens/ (TransmitterScreen, HistoryScreen)
│   ├── components/ (9 UI Components)
│   └── MainActivity.kt
└── ui/theme/ (Dimensions.kt - Design Tokens)
```

### 🎨 Design Token System
- `AppDimensions.kt` mit allen Größen, Spacing, Corner Radius
- Keine hardcoded Werte mehr
- Konsistentes Design durch das gesamte Projekt

### ✅ Bugs Behoben
- **History speichert jetzt korrekt** (Repository Pattern)
- **Morse-to-Text Übersetzung funktioniert** (korrekte Spacing-Logik)
- **Signal Transmission nutzt richtigen Morse-Code**

### 📝 Dokumentation
- README.md in jedem Package
- KDoc Kommentare für alle öffentlichen Klassen/Funktionen
- Erklärung der Verantwortlichkeiten jeder Component

### 🧩 Components (einzelne Dateien)
1. InputCard.kt - Dual-Mode Input (Text/Morse)
2. OutputCard.kt - Translation Display
3. SignalVisualizer.kt - Visual Feedback
4. InputModeSelector.kt - Mode Toggle
5. OutputSelector.kt - Output Method Chips
6. PlayButton.kt - Main Action Button
7. HistoryItem.kt - List Item
8. MorseKeyboard.kt - Custom Keyboard
9. CameraScannerMockUI.kt - Scanner UI

### 🔧 Technische Verbesserungen
- ViewModel verwendet jetzt Repository Pattern
- Hardware-Zugriff ausgelagert in MorseTransmitter
- Business Logic getrennt in MorseDictionary
- State Management zentralisiert im ViewModel


## 29/11/2024 - 21:15 - Animated Playback Progress

### ✨ Neue Features
- **Animierter Playback Progress in OutputCard**
  - Bereits gespielte Morse-Zeichen werden ausgegraut (30% Opacity)
  - Aktuell gespieltes Zeichen wird hervorgehoben (100% Opacity)
  - Smooth Animationen mit `animateFloatAsState`
  - Nach Playback-Ende: Alles wieder normal

### 🔧 Technische Implementierung
- **MorseTransmitter**: Neuer `onPlaybackProgress` Callback
  - Benachrichtigt UI über aktuellen Character-Index
  - Wird bei jedem gespielten Zeichen aufgerufen
  
- **ViewModel**: Neuer `playbackIndex` State
  - Tracked welcher Character gerade gespielt wird
  - -1 = nicht spielend (default)
  
- **OutputCard**: Komplett überarbeitet
  - Neue `AnimatedMorseText` Composable
  - Verwendet `buildAnnotatedString` für per-character styling
  - Smooth Opacity-Transitions pro Zeichen

### 🎨 UX Verbesserungen
- User sieht genau wo im Code die Wiedergabe gerade ist
- Besseres Verständnis des Morse-Timings
- Visuell ansprechende Feedback-Animation


## 29/11/2024 - 21:45 - Kotlin Multiplatform Refactoring

### 🎯 Projekt ist jetzt echtes Kotlin Multiplatform!

**Vorher:** Code war nur in `androidMain` → nicht iOS-kompatibel  
**Jetzt:** Saubere Trennung `commonMain` vs `androidMain`

### 📁 Neue Multiplatform-Struktur

#### commonMain (Platform-agnostic Code)
- ✅ **data/** - Models & Repository (shared)
- ✅ **domain/** - Business Logic (shared)
  - MorseDictionary: Übersetzung
  - MorseTransmitter: expect/actual Pattern
- ✅ **presentation/** - Komplettes UI (shared!)
  - viewmodel/
  - screens/
  - components/
- ✅ **ui/theme/** - Design System (shared)

#### androidMain (Android-specific)
- ✅ **MainActivity** - Android Entry Point
- ✅ **MorseTransmitter (actual)** - Android Hardware
- ✅ **PlatformContext** - Context Wrapper
- ✅ **Platform.android.kt** - Platform Info

#### iosMain (Vorbereitet für iOS)
- 🔜 MainViewController
- 🔜 MorseTransmitter (actual) - iOS Hardware

### 🔧 Technische Highlights

**expect/actual Pattern:**
```kotlin
// commonMain
expect class MorseTransmitter() {
    suspend fun transmit(...)
}

// androidMain
actual class MorseTransmitter {
    // Android Vibrator + Camera Flash Implementation
}
```

**Platform Context:**
```kotlin
// Initialize in MainActivity
initializeHardware(this)  // Android Context

// Later use in Transmitter
val ctx = getAndroidContext()
```

### ✅ Vorteile

1. **iOS-Ready:** UI-Code ist bereits iOS-kompatibel
2. **Code-Sharing:** ~95% Code-Sharing zwischen Platforms
3. **Klare Trennung:** Platform-Code nur wo nötig
4. **Wartbar:** Saubere Architektur

### 📊 Code-Verteilung

- commonMain: **18 Files** (Data, Domain, UI)
- androidMain: **4 Files** (Platform-specific)
- Sharing Rate: **~95%**

