===== FILE: design-spec-morse-app.md =====
---
title: "Morse App — UI/UX Design Spec"
version: 1.0.0
last_updated: 2025-11-16
platform: "Android · Material 3 (Material You / Expressive)"
author: "UX / UI"
---

# 0. Überblick

**Kurzbeschreibung**

Morse-App als "Übersetzer" zwischen **Text ↔ Morse**, mit multimodaler Ausgabe:
- Screen-Visualisierung (Morse-Timeline)
- Flashlight
- Vibration
- Audio

Fokus:
- Sofort erfassbare, einfache UX (ähnlich Translate)
- Visuell eigenständig, aber klar Material 3 / Expressive
- Multimodale Ausgabe klar, transparent und kontrollierbar

---

# 1. Produkt & UX-Ziele

## 1.1 Produktziele

- Text zu Morse und Morse zu Text so einfach wie eine Übersetzungs-App machen.
- Morse als **Rhythmus** erlebbar machen (visuell, light, haptic, audio).
- Für Einsteiger verständlich, für Nerds nicht peinlich.

## 1.2 UX-Prinzipien

1. **Ein Screen zuerst**  
   Primäre Nutzung findet im **Home/Translate Screen** statt.
2. **Multimodale Klarheit**  
   Jeder Kanal (Flash/Vibration/Sound/Visual) ist:
   - sichtbar, schaltbar, klar benannt
   - beim Playback synchron visualisiert
3. **Material Expressive, aber kontrolliert**  
   Farben, Typografie, Motion als Akzent — nie zulasten der Lesbarkeit.
4. **Safety & Accessibility zuerst**  
   Kein aggressives Flackern, sensible Defaults, opt-out einfach.

---

# 2. Informationsarchitektur

## 2.1 Screens

1. **Home: Translate**
   - Modus: Text → Morse / Morse → Text
   - Input-Bereich
   - Output-Bereich
   - Playback-Bereich (Kanäle & Play)

2. **History**
   - Liste früherer Übersetzungen
   - Re-Load in den Home Screen

3. **Learn / Playground (optional v1.1+)**
   - Alphabet, Zahlen, Übungen

4. **Settings**
   - Multimodale Ausgabekonfiguration
   - Geschwindigkeit, Intensität, Safety
   - Design/Theme-Optionen (Material You)

## 2.2 Navigation

- **Primary:** Single-Activity, Top App Bar + Overflow
- Home ist Default Start.
- History und Settings über Top App Bar Icons:
  - Left: History (Clock-Icon)
  - Right: Settings (Gear-Icon)

---

# 3. Design Tokens

> Hinweis: Alle Werte in `dp` (Layout), `sp` (Font), `ms` (Zeit).  
> Farbwerte sind beispielhaft, sollten idealerweise mit Material-You-Dynamic-Color gemappt werden.

## 3.1 Farb-Tokens

### 3.1.1 Semantische Farben

| Token                         | Rolle                                      | Beispielwert  |
|------------------------------|--------------------------------------------|---------------|
| `color.primary`              | Hauptakzent (Buttons, aktiver Modus)      | #6750A4       |
| `color.onPrimary`            | Text/Icon auf Primary                      | #FFFFFF       |
| `color.secondary`            | Sekundärer Akzent (Chips, Learn)          | #625B71       |
| `color.onSecondary`          | Text auf Secondary                         | #FFFFFF       |
| `color.background`           | App-Hintergrund                            | #FFFBFE       |
| `color.surface`              | Default Card-Hintergrund                   | #FEF7FF       |
| `color.surfaceVariant`       | Sekundäre Cards, Panels                   | #E7E0EC       |
| `color.onSurface`            | Primärtext                                 | #1D1B20       |
| `color.onSurfaceVariant`     | Sekundärtext, Labels                       | #49454F       |
| `color.outline`              | Divider, leichte Rahmen                    | #79747E       |
| `color.error`                | Fehlerzustände                             | #B3261E       |
| `color.onError`              | Text auf Error                             | #FFFFFF       |

### 3.1.2 App-spezifische Farben

| Token                              | Rolle                                            | Beispielwert |
|-----------------------------------|--------------------------------------------------|--------------|
| `color.morseDot`                  | Visualisation: Punkt                             | #1D1B20      |
| `color.morseDash`                 | Visualisation: Strich                            | #1D1B20      |
| `color.morseGap`                  | Visualisation: Lücke                             | #CAC4D0      |
| `color.channelFlash`              | Chip/Icon für Flash-Output                       | #FFB300      |
| `color.channelVibration`          | Chip/Icon für Vibration                          | #00C853      |
| `color.channelSound`              | Chip/Icon für Sound                              | #2962FF      |
| `color.channelVisual`             | Chip/Icon für on-screen Visualisierung           | #AA00FF      |
| `color.channelInactiveBackground` | Inaktive Chip-Fill                               | #E7E0EC      |
| `color.channelInactiveContent`    | Inaktiver Chip-Text/Icon                         | #49454F      |
| `color.channelActiveBackground`   | Aktiver Chip-Fill (leicht getönt nach Channel)   | Channel-Farbton |
| `color.channelActiveContent`      | Aktiver Chip-Text/Icon                           | #FFFFFF      |

### 3.1.3 States

| Token                        | Rolle                                      |
|-----------------------------|--------------------------------------------|
| `color.state.focusRing`     | Fokus-Ring / A11y-Highlight                |
| `color.state.pressedOverlay`| Aufdruck bei Press                         |
| `color.state.disabled`      | Disabled-Text/Icon                         |
| `color.state.disabledBg`    | Disabled-Background                        |

---

## 3.2 Typografie Tokens

Baseline: Material 3 Typo + App-spezifische Morse-Styles.

**Font-Familien:**
- `font.primary`: Roboto (oder Google Sans im Brand-Kontext)
- `font.mono`: Roboto Mono (für Morse und technische Darstellung)

### 3.2.1 Global Typo

| Token                    | Font           | Größe (sp) | Gewicht | Zeilenhöhe (sp) | Nutzung                         |
|--------------------------|----------------|------------|---------|------------------|----------------------------------|
| `type.display.lg`        | primary        | 32         | 600     | 40               | App-Titel, Onboarding Headlines |
| `type.headline.md`       | primary        | 24         | 600     | 32               | Screen-Titel in Cards           |
| `type.title.lg`          | primary        | 20         | 600     | 28               | Section-Titel                   |
| `type.title.md`          | primary        | 16         | 600     | 24               | Card-Titel                      |
| `type.body.lg`           | primary        | 16         | 400     | 24               | Haupttext                        |
| `type.body.md`           | primary        | 14         | 400     | 20               | Sekundärtext, Hilfetexte        |
| `type.label.md`          | primary        | 12         | 500     | 16               | Labels, Chip-Text               |
| `type.label.sm`          | primary        | 11         | 500     | 14               | Meta-Info, Badges               |

### 3.2.2 Morse-spezifische Typo

| Token                       | Font   | Größe (sp) | Gewicht | Zeilenhöhe (sp) | Nutzung                               |
|-----------------------------|--------|------------|---------|------------------|----------------------------------------|
| `type.morse.outputLarge`    | mono   | 22         | 500     | 28               | Haupt-Morse-Zeile im Output           |
| `type.morse.outputInline`   | mono   | 16         | 500     | 22               | Morse in Chips / Inline               |
| `type.morse.timelineLabel`  | mono   | 12         | 400     | 16               | Kleine Infos in der Timeline          |
| `type.morse.letterPreview`  | mono   | 14         | 500     | 18               | pro Buchstaben-Gruppierung            |

---

## 3.3 Spacing Tokens

Baseline: 4dp-Grid.

| Token           | Wert (dp) | Verwendung                                                   |
|-----------------|-----------|--------------------------------------------------------------|
| `space.0`       | 0         | Keine Abstände                                               |
| `space.xs`      | 4         | Dichte Elemente, Icon-Padding                                |
| `space.sm`      | 8         | Innerhalb kleiner Controls                                  |
| `space.md`      | 12        | Zwischen verwandten Elementen (Label zu Field, Chips)       |
| `space.lg`      | 16        | Zwischen Sections in Cards                                  |
| `space.xl`      | 24        | Zwischen Cards / Haupt-Layoutbereiche                       |
| `space.2xl`     | 32        | Großer Abstand (Top Padding unter App Bar)                  |

**Layout-Konstanten**

| Token               | Wert (dp) | Beschreibung                        |
|---------------------|-----------|-------------------------------------|
| `layout.screenPadding.horizontal` | 16 | Linker/rechter Screen Rand     |
| `layout.screenPadding.vertical`   | 16 | Ober-/Unter-Abstand (Content)  |
| `layout.cardPadding`              | 16 | Innenabstand Cards             |
| `layout.chipSpacing`             | 8  | Abstand zwischen Channel Chips |
| `layout.sectionGap`              | 24 | Abstand zwischen großen Sektionen |

---

## 3.4 Radius & Elevation

### 3.4.1 Corner-Radius

| Token                  | Wert (dp) | Elemente                                  |
|------------------------|-----------|-------------------------------------------|
| `radius.xs`            | 4         | Kleine Chips, Badges                      |
| `radius.sm`            | 8         | Textfelder, kleine Cards                  |
| `radius.lg`            | 16        | Haupt-Cards im Home Screen                |
| `radius.pill`          | 999       | Segmented Control, Play-Button (Pill)     |

### 3.4.2 Elevation

| Token                  | Wert (dp) | Nutzung                               |
|------------------------|-----------|----------------------------------------|
| `elevation.none`       | 0         | Hintergrund, flache Flächen           |
| `elevation.card`       | 1         | Standard Cards                         |
| `elevation.cardRaised` | 3         | Hover/Focus bzw. aktive Cards         |
| `elevation.bottomPanel`| 4         | Playback-Bereich (Sticky)             |
| `elevation.fab`        | 6         | Falls FAB verwendet wird              |

---

## 3.5 Motion & Timing

### 3.5.1 Global Motion Tokens

| Token                          | Wert (ms) | Verwendung                                      |
|--------------------------------|-----------|-------------------------------------------------|
| `motion.duration.xs`           | 100       | Micro-Feedback (Button Press)                   |
| `motion.duration.sm`           | 150       | Chip-State-Wechsel                              |
| `motion.duration.md`           | 220       | Card-Transitions, Moduswechsel                  |
| `motion.duration.lg`           | 300       | Onboarding-Transitions                          |
| `motion.easing.standard`       | cubic-bezier(0.2, 0, 0, 1) | Standard-Enter/Exit       |
| `motion.easing.emphasized`     | cubic-bezier(0.3, 0, 0, 1) | Für Play-Start-Animation |
| `motion.easing.decelerate`     | cubic-bezier(0, 0, 0, 1)   | Für Stop/Abbremsen       |

### 3.5.2 Morse-spezifische Timing Tokens

Diese Werte sind Defaults, sollten in Settings justierbar sein.

| Token                          | Rolle                               | Default-Wert |
|--------------------------------|-------------------------------------|--------------|
| `morse.unit.dot`               | Dauer eines Punktes                 | 120 ms       |
| `morse.unit.dash`              | Dauer eines Strichs (3×dot)         | 360 ms       |
| `morse.unit.gap.intraChar`     | Abstand zwischen Zeichen im Buchstaben | 120 ms   |
| `morse.unit.gap.interChar`     | Abstand zwischen Buchstaben         | 360 ms       |
| `morse.unit.gap.word`          | Abstand zwischen Wörtern            | 840 ms       |

---

## 3.6 Icon- & Control-Sizes

| Token                | Wert     | Nutzung                       |
|----------------------|----------|-------------------------------|
| `size.icon.sm`       | 18 dp    | Sekundäre Icons               |
| `size.icon.md`       | 24 dp    | Standard Icons (AppBar, Chips)|
| `size.icon.lg`       | 32 dp    | Playback Icons (Play/Stop)    |
| `size.control.sm`    | 32 × 32  | Kleinere Icon Buttons         |
| `size.control.md`    | 40 × 40  | Standard Icon Buttons         |
| `size.control.lg`    | 56 × 56  | Großer Morse-Tap-Button       |

---

# 4. Layout-Spezifikation: Key Screens

## 4.1 Home Screen – Translate

### 4.1.1 Struktur (Portrait, 1-Spalten-Layout)

- Top App Bar (Large / Center Aligned)
- Segmented Control: Modus-Auswahl
- Input Card
- Output Card
- Playback Panel (sticky bottom)

### 4.1.2 ASCII-Wireframe

┌───────────────────────────────────────┐
│  Morse                                │  <-- Top App Bar (Large)
│  [History]                    [⚙]     │
├───────────────────────────────────────┤
│ [ Text → Morse ] [ Morse → Text ]     │  <-- Segmented Control (Pill)
├───────────────────────────────────────┤
│ [Card] Input                          │
│  Label                                │
│  Multiline TextField / Morse-Tap      │
└───────────────────────────────────────┘
┌───────────────────────────────────────┐
│ [Card] Output                         │
│  Title                                │
│  Morse/Text Output                    │
│  Optional: Visual Timeline Preview    │
└───────────────────────────────────────┘
┌───────────────────────────────────────┐
│ [◼]  [▶ PLAY]   [Flash] [Vibration]   │  <-- Playback Panel
│                   [Sound] [Visual]    │
└───────────────────────────────────────┘

### 4.1.3 Layout-Details

| Section         | Padding                         | Besonderheiten                                |
|----------------|----------------------------------|-----------------------------------------------|
| Screen         | `layout.screenPadding`           | Oben zusätzl. 8 dp unter App Bar             |
| Input Card     | `layout.cardPadding`            | Vertikal: 16 dp, Horizontal: 16 dp           |
| Output Card    | `layout.cardPadding`            | Identisch Input                               |
| Segmented Ctrl | 16 dp Abstand unter App Bar      | Höhe ~40 dp, Pill-Radius                      |
| Playback Panel | 16 dp padding, `elevation.bottomPanel` | Sticky, über Systemnav / gestured angepasst |

---

## 4.2 History Screen

- Top App Bar: Titel “History”
- Liste als `LazyColumn` / `RecyclerView`:
  - Jede Zeile: Icon + Preview + Action

| Element       | Token / Größe                   |
|---------------|----------------------------------|
| Row Höhe      | 64 dp                            |
| Icon          | `size.icon.md` links            |
| Primary Text  | `type.body.lg`                  |
| Secondary Text| `type.body.md` (optional)       |
| Right Icon    | Play-Icon `size.icon.md`        |
| Spacing       | Horizontal Padding 16 dp        |

---

## 4.3 Settings Screen (High-Level)

Sections:
1. Output Channels
2. Morse Speed
3. Safety
4. Theme

Jede Section als Card mit Titel (`type.title.md`) und Inline-Controls (Switches, Sliders).

---

# 5. Komponenten-Spezifikation

## 5.1 Segmented Control – Moduswahl

### Beschreibung

Zweistufiges Toggle:  
- Option A: **Text → Morse**  
- Option B: **Morse → Text**

### Visual

- Hintergrund: `color.surfaceVariant`
- Pill-Container mit `radius.pill`
- Aktiver Segment:
  - Fill: `color.primary`
  - Text: `color.onPrimary`
- Inaktiver Segment:
  - Text: `color.onSurface`
  - Hintergrund: transparent

### States

| State    | Darstellung                                                      |
|----------|------------------------------------------------------------------|
| Default  | Wie oben beschrieben                                            |
| Pressed  | Leichter Overlay (`color.state.pressedOverlay`)                 |
| Focused  | Fokus-Ring (`color.state.focusRing`)                            |
| Disabled | Text `color.state.disabled`, geringere Opazität Hintergrund     |

---

## 5.2 Input Card – Text → Morse

### Struktur

- Card (`radius.lg`, `elevation.card`)
- Content:
  - Label: “Gib deinen Text ein”
  - Multiline TextField
  - Optional: Character Count / Hint

### Tokens

| Aspekt      | Token                       |
|-------------|-----------------------------|
| Background  | `color.surface`             |
| Titel       | `type.title.md`             |
| Label Text  | `type.label.md`             |
| Input Text  | `type.body.lg`              |
| Padding     | `layout.cardPadding`        |
| Radius      | `radius.lg`                 |

---

## 5.3 Input Card – Morse → Text

### Struktur

- Card ähnlich wie oben, aber Inhalt:
  - Label: “Tippe Morse (kurz = ·, lang = −)”
  - Großer Tap-Button
  - Grafik / kleine Timeline für aktuell eingegebenes Zeichen

### Tap-Button

| Aspekt      | Spezifikation                                    |
|-------------|--------------------------------------------------|
| Größe       | `size.control.lg`                                |
| Form        | Kreis (`radius.pill`)                            |
| Icon        | Punkt/Strich-Icon oder simples “• −” Symbol      |
| Background  | `color.primary`                                  |
| Content     | `color.onPrimary`                                |

**Interaction:**
- Tap < `morse.unit.dot` → Punkt
- Tap ≥ `morse.unit.dot` und < `morse.unit.dash` → Strich  
  (Genaues Mapping in Logic-Spec dokumentieren)
- Kurze Visual-Aura beim Tap (Scale + Opacity)

---

## 5.4 Output Card

### Struktur

- Card (`radius.lg`, `elevation.card`)
- Header:
  - Titel: “Morse Code” / “Text”
  - Actions: Copy, Share (Icons rechts)
- Content:
  - Primäre Darstellung (Morse oder Text)
  - Optional: Aufteilung in Chips pro Buchstabe
  - Optional: Button “Visual Timeline anzeigen”

### Tokens

| Aspekt             | Token                         |
|--------------------|-------------------------------|
| Titel              | `type.title.md`               |
| Primärinhalt Text  | `type.body.lg`                |
| Primärinhalt Morse | `type.morse.outputLarge`      |
| Chips              | `radius.xs`, `type.morse.outputInline` |
| Background         | `color.surface`               |

---

## 5.5 Output Channel Chips

Kanalsteuerung am Playback Panel: Flash, Vibration, Sound, Visual.

### Visual

- Basis: Filter Chips (Material 3)
- Form: `radius.pill`
- Default Fill: `color.channelInactiveBackground`
- Default Text/Icon: `color.channelInactiveContent`

### Aktive States per Kanal

| Kanal     | Background                      | Content                  |
|-----------|---------------------------------|--------------------------|
| Flash     | `color.channelFlash`            | `color.channelActiveContent` |
| Vibration | `color.channelVibration`        | `color.channelActiveContent` |
| Sound     | `color.channelSound`            | `color.channelActiveContent` |
| Visual    | `color.channelVisual`           | `color.channelActiveContent` |

### States

| State    | Verhalten                                                                 |
|----------|---------------------------------------------------------------------------|
| Default  | siehe oben                                                                |
| Pressed  | Scale 0.96 + Overlay (`motion.duration.xs`)                               |
| Selected | Fills aktiv, ggf. leichte regelmässige Pulsation beim Playback           |
| Disabled | Alpha 0.38, keine Interaktion (z.B. wenn Berechtigung fehlt)             |

---

## 5.6 Playback Panel

### Struktur

- Container: Sticky Bottom Panel (`elevation.bottomPanel`)
- Inhalt:
  - Left: Stop/Play Icon Button (`◼` wenn aktiv, `▶` wenn idle)
  - Center: Primary Button “PLAY”
  - Right / 2nd Row: Channel Chips

### Tokens

| Aspekt         | Token                         |
|----------------|-------------------------------|
| Background     | `color.surface`               |
| Padding        | `layout.screenPadding`        |
| Button Height  | 40 dp                         |
| Button Typo    | `type.label.md`               |
| Play Icon Size | `size.icon.lg`                |

---

## 5.7 Visual Timeline

### Struktur

- Horizontaler Balken in Output Card oder Full-Width im Playback Panel
- Segment-Typen:
  - Dot Segment: kurz, gefüllt `color.morseDot`
  - Dash Segment: 3× so lang, gefüllt `color.morseDash`
  - Gap: Leerer Bereich auf `color.morseGap`

### Verhalten

- Beim Playback:
  - Animierter Cursor fährt über Timeline.
  - Aktives Segment leicht aufgehellt / skaliert.
- Interaktionen:
  - V1: nur Anzeige
  - V1.1+: Optional: Scrub für Seek

---

# 6. Interaction-Spec

## 6.1 Play Flow (Text → Morse)

1. Nutzer gibt Text ein oder lädt aus History.
2. Output Card zeigt Morse (live beim Tippen).
3. Nutzer wählt Kanäle (Chips).
4. Nutzer tippt auf **PLAY**:
   - Play-Button wechselt zu aktivem Zustand (Farbe/Elevation).
   - Timeline startet, Cursor bewegt sich.
   - Kanäle feuern synchron:
     - Flash: Helligkeit an/aus entsprechend Dot/Dash.
     - Vibration: Vibrationspattern gem. Morse-Dauern.
     - Sound: Ton aktive Phase, Stille Gaps.
5. Nutzer kann jederzeit:
   - Stop drücken → Playback stoppt, Timeline Cursor am Anfang.
   - Panel schließen (falls erweitert) → Playback ebenfalls stoppen.

## 6.2 Moduswechsel

- Beim Wechsel `Text → Morse` ↔ `Morse → Text`:
  - Shared-Axis-Transition (X-Achse) zwischen Input Cards.
  - Output Card Inhalt cross-faded, Dauer `motion.duration.md`.
  - Current Input wird **nicht** automatisch konvertiert, es sei denn explizit gewünscht (klar definieren).

---

# 7. Accessibility & Safety

## 7.1 Flash Safety

Settings-Optionen:

- Switch: **“Flash-Kanal erlauben”**
- Switch: **“Flacker-reduzierter Modus (empfohlen)”**
- Im reduzierten Modus:
  - Max Frequenz begrenzen (z.B. Dot >= 160ms, keine extrem kurzen Strobes).
  - Sanfte Übergänge (Fade) statt harter ON/OFF möglich.

## 7.2 Vibration & Sound

- Slider: Intensität (wenn vom System unterstützt).
- Slider: Lautstärke (oder System Volume-Hinweis).
- Hinweistexte: Nutzung von System-Einstellungen respektieren.

## 7.3 Screenreader & Fokus

- Alle Controls mit aussagekräftigen Labels:
  - Play-Button: “Morse abspielen”
  - Stop-Button: “Wiedergabe stoppen”
  - Chips: “Flash-Ausgabe aktivieren/deaktivieren”
- Fokus-Reihenfolge:
  - App Bar → Modus → Input → Output → Playback.

## 7.4 Schriftgröße & Skalierung

- Alle Texte in `sp`.
- Layouts so bauen, dass bis mind. 130–150% System-Schriftgröße funktionieren:
  - Cards wachsen vertikal, Scroll statt Clipping.

---

# 8. Beispielhafte Token-Definition (JSON)

> Als Referenz für Devs, wie die Tokens in Code gegossen werden können.

{
  "color": {
    "primary": "#6750A4",
    "onPrimary": "#FFFFFF",
    "background": "#FFFBFE",
    "surface": "#FEF7FF",
    "surfaceVariant": "#E7E0EC",
    "onSurface": "#1D1B20",
    "morseDot": "#1D1B20",
    "morseDash": "#1D1B20",
    "morseGap": "#CAC4D0",
    "channelFlash": "#FFB300",
    "channelVibration": "#00C853",
    "channelSound": "#2962FF",
    "channelVisual": "#AA00FF"
  },
  "spacing": {
    "xs": 4,
    "sm": 8,
    "md": 12,
    "lg": 16,
    "xl": 24,
    "xxl": 32
  },
  "radius": {
    "xs": 4,
    "sm": 8,
    "lg": 16,
    "pill": 999
  },
  "motion": {
    "duration": {
      "xs": 100,
      "sm": 150,
      "md": 220,
      "lg": 300
    },
    "morse": {
      "dot": 120,
      "dash": 360,
      "gapIntraChar": 120,
      "gapInterChar": 360,
      "gapWord": 840
    }
  },
  "typography": {
    "bodyLg": { "size": 16, "lineHeight": 24, "weight": 400 },
    "morseOutputLarge": { "size": 22, "lineHeight": 28, "weight": 500, "fontFamily": "Roboto Mono" }
  }
}

---

# 9. Offene Punkte / Implementierungsnotizen

- **Morse-Eingabe-Erkennung**: exakte Schwellenwerte (Tap-Länge vs. Dot/Dash) müssen mit Devs & evtl. User-Tests feinjustiert werden.
- **Timeline-Sync**: Single Source of Truth für Morse-Unit-Timing (Token `morse.unit.*`).
- **Theming**: Mapping dieser Tokens auf Android Material Theme (ColorScheme, Typography, Shape).

---

# 10. Changelog

- **1.0.0 (2025-11-16)**  
  Erstversion der Design-Spec für:
  - Design Tokens
  - Key Layouts
  - Komponenten
  - Interactions
  - Accessibility


