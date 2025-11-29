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
