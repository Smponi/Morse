# UI Components

Alle wiederverwendbaren UI-Komponenten der App.

## 📦 Komponenten-Übersicht

### Input Components

#### `InputCard.kt`
Haupt-Eingabefeld der App
- **TEXT Modus**: Editierbares Textfeld
- **MORSE Modus**: Read-Only Morse Terminal mit großer Schrift
- Inkl. Camera Scanner Button

#### `InputModeSelector.kt`
Toggle zwischen TEXT und MORSE Modus
- Tab-basiertes Design
- Smooth Animationen

### Output Components

#### `OutputCard.kt`
Zeigt Übersetzungs-Ergebnis
- TEXT Modus: Morse-Code
- MORSE Modus: Text

#### `SignalVisualizer.kt`
Visuelles Feedback für Signal-Status
- Pulsiert bei aktiven Signalen
- Color: Primary (aktiv) / Surface Variant (inaktiv)

### Input Method Components

#### `MorseKeyboard.kt`
Custom Keyboard für Morse-Eingabe
- Große DOT und DASH Pads (160dp hoch)
- Space Button (trennt Buchstaben)
- Delete Button
- Gradient-Hintergrund mit dekorativem Kreis

### Action Components

#### `PlayButton.kt`
Haupt-Action-Button
- "TRANSMIT SIGNAL" (nicht spielend)
- "STOP TRANSMISSION" (spielend)
- Haptic Feedback

#### `OutputSelector.kt`
Auswahl der Output-Methoden
- FilterChips für VIBRATION, LIGHT, SOUND
- Multi-Select möglich

### List Components

#### `HistoryItem.kt`
Einzelner Eintrag in History/Favorites
- Zeigt Text + Morse + Timestamp
- Favorite Toggle Button
- Click zum Laden

### Special Components

#### `CameraScannerMockUI.kt`
Mock UI für OCR Scanner
- Animierte Scan-Linie
- TODO: Später durch echte CameraX + ML Kit ersetzen

## 🎨 Design Tokens

**ALLE Components verwenden `AppDimensions` aus `ui/theme/Dimensions.kt`!**

Beispiele:
- Spacing: `AppDimensions.Spacing.medium`
- Heights: `AppDimensions.Height.buttonLarge`
- Corner Radius: `AppDimensions.CornerRadius.large`
- Icons: `AppDimensions.IconSize.medium`

## 📐 Verwendung

```kotlin
// Import
import com.mouse.mouse.presentation.components.InputCard

// Verwendung
InputCard(
    text = viewModel.displayTop,
    label = "TEXT INPUT",
    isReadOnly = false,
    onTextChange = { viewModel.onTextInputChange(it) },
    onCameraClick = { /* ... */ }
)
```

## ✅ Best Practices

1. **Kommentare**: Jede Component hat KDoc Header
2. **Parameters**: Alle Parameter haben Beschreibungen
3. **Design Tokens**: Keine hardcoded dp/sp Werte
4. **Single Responsibility**: Jede Component macht nur 1 Sache
5. **Reusability**: Components sind unabhängig verwendbar
