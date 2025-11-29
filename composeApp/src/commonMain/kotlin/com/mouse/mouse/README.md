# Morse App - Kotlin Multiplatform

## 🎯 Projekt-Struktur

Dieses Projekt nutzt **Kotlin Multiplatform** für iOS & Android Code-Sharing.

### 📱 Platforms

- ✅ **Android** - Voll funktionsfähig
- 🔜 **iOS** - Vorbereitet (TODO: iOS actual implementations)

## 📂 commonMain (Shared Code - ~95%)

Dieser Code läuft auf **allen Platforms**:

```
com.mouse.mouse/
├── data/                      # Daten-Layer
│   ├── model/                # Datenmodelle
│   │   ├── InputMode.kt      # TEXT vs MORSE
│   │   ├── OutputMode.kt     # VIBRATION, LIGHT, SOUND
│   │   └── MorseRecord.kt    # History Entry
│   └── repository/
│       └── MorseHistoryRepository.kt  # History Management
│
├── domain/                    # Business Logic
│   ├── MorseDictionary.kt    # Text ↔ Morse Translation
│   └── MorseTransmitter.kt   # expect class (platform-specific)
│
├── presentation/              # UI Layer
│   ├── MainActivity.kt        # App Entry
│   ├── viewmodel/
│   │   └── MorseSuiteViewModel.kt  # State Management
│   ├── screens/
│   │   ├── TransmitterScreen.kt    # Haupt-Screen
│   │   └── HistoryScreen.kt        # History/Favorites
│   └── components/            # UI Components (9 Files)
│       ├── InputCard.kt       # Dual-Mode Input
│       ├── OutputCard.kt      # Animated Output
│       ├── SignalVisualizer.kt
│       ├── InputModeSelector.kt
│       ├── OutputSelector.kt
│       ├── PlayButton.kt
│       ├── HistoryItem.kt
│       ├── MorseKeyboard.kt   # Custom Keyboard
│       └── CameraScannerMockUI.kt
│
├── ui/theme/                  # Design System
│   ├── Dimensions.kt          # Design Tokens
│   ├── Color.kt
│   ├── Theme.kt
│   └── Type.kt
│
└── platform/                  # Platform Abstraction
    └── PlatformContext.kt     # expect fun
```

## 📱 androidMain (Android-specific - ~5%)

Platform-spezifischer Code für Android:

```
com.mouse.mouse/
├── domain/
│   └── MorseTransmitter.kt    # actual class (Android Hardware)
├── platform/
│   └── PlatformContext.android.kt  # actual fun
├── presentation/
│   └── MainActivity.kt        # Android Activity (moved from common)
└── Platform.android.kt
```

### 🔑 Android-Spezifisch

- **Vibrator API** - Android Vibration System
- **Camera2 API** - Flashlight/Torch Control
- **Activity** - Android Lifecycle

## 🍎 iosMain (iOS-specific - TODO)

Wird später implementiert:

```
com.mouse.mouse/
├── domain/
│   └── MorseTransmitter.kt    # actual class (iOS Haptics)
├── platform/
│   └── PlatformContext.ios.kt
└── MainViewController.kt
```

### 🔑 iOS-Spezifisch (TODO)

- **CoreHaptics** - iOS Haptic Engine
- **AVFoundation** - Flashlight Control
- **UIViewController** - iOS Lifecycle

## 🎨 Design Tokens

**Alle Größen sind in `ui/theme/Dimensions.kt` zentralisiert:**

```kotlin
// ✅ IMMER verwenden
AppDimensions.Spacing.medium
AppDimensions.Height.buttonLarge
AppDimensions.CornerRadius.large

// ❌ NIEMALS hardcoded
16.dp
64.dp
24.dp
```

## 🏗️ Architektur

### Layer-Trennung

```
┌─────────────────────────────────────┐
│  Presentation Layer (UI + ViewModel) │  ← commonMain
├─────────────────────────────────────┤
│  Domain Layer (Business Logic)       │  ← commonMain (expect/actual)
├─────────────────────────────────────┤
│  Data Layer (Models + Repository)    │  ← commonMain
└─────────────────────────────────────┘
         ↓              ↓
   ┌──────────┐  ┌──────────┐
   │ Android  │  │   iOS    │  ← platform Main
   └──────────┘  └──────────┘
```

### Datenfluss

```
User Input (Compose UI)
    ↓
Screen (commonMain)
    ↓
ViewModel (commonMain)
    ↓
Domain Logic (commonMain)
    ↓
Transmitter (expect)
    ↓
Platform Implementation (actual)
    ↓
Hardware (Android Vibrator/Camera OR iOS Haptics/AVFoundation)
```

## 🚀 Nächste Schritte (iOS)

1. [ ] iOS MorseTransmitter Implementation
   - CoreHaptics für Vibration
   - AVFoundation für Flashlight
   
2. [ ] iOS MainViewController
   - SwiftUI-Wrapper für Compose UI
   
3. [ ] iOS Platform.ios.kt
   - Device Info

4. [ ] iOS Testing
   - Simulator + Real Device

## 📝 Coding Guidelines

### Neue Dateien erstellen

**commonMain** für:
- UI Components (Compose Multiplatform!)
- Business Logic
- Models & Repositories
- Alles was platform-agnostic ist

**androidMain/iosMain** nur für:
- Hardware-Zugriffe
- Platform-specific APIs
- Lifecycle-Code (Activity, ViewController)

### expect/actual Pattern

```kotlin
// commonMain - Interface
expect class MyPlatformClass {
    fun doSomething()
}

// androidMain - Implementation
actual class MyPlatformClass {
    actual fun doSomething() {
        // Android-specific code
    }
}

// iosMain - Implementation  
actual class MyPlatformClass {
    actual fun doSomething() {
        // iOS-specific code
    }
}
```

## ✅ Erfolge

- ✅ **21 Files** in sauberer Struktur
- ✅ **4 READMEs** mit Dokumentation
- ✅ **Design Token System**
- ✅ **Animierte Playback Progress**
- ✅ **95% Code-Sharing** Android ↔ iOS
- ✅ **Clean Architecture** Pattern
- ✅ **KDoc Kommentare** überall
