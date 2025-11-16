===== FILE: design-spec-morse-app-flows.md =====
---
title: "Morse App — Flow & Edge Case Spec"
version: 1.0.0
last_updated: 2025-11-16
platform: "Android · Material 3 (Material You / Expressive)"
author: "UX / UI"
related_docs:
  - "design-spec-morse-app.md"
---

# 0. Scope / Zweck

Dieses Dokument beschreibt:

- **User Flows** für die wichtigsten Use Cases:
  - First-Run & Onboarding
  - Text → Morse
  - Morse → Text
  - Playback (Flash, Vibration, Sound, Visual)
  - History
  - Settings
  - (optional) Learn / Playground
- **Zustandsmodell** für die Wiedergabe
- **Fehlerzustände & Edge Cases**
- **Permissions & Safety** (Flash, Vibration, Sound)
- **Persistenz & Privacy-Grundregeln**

Ziel: Wer nach 2+ Monaten wieder ins Projekt steigt, kann mit diesem Dokument + der UI-Spec direkt die End-to-End-UX verstehen und neue Stories sauber anschließen.

---

# 1. High-Level Flows

## 1.1 First Run / Onboarding

**Ziel:**  
User versteht in < 30 Sekunden:
- was die App macht,
- dass Morse “Rhythmus” ist,
- welche Kanäle benutzt werden können,
- dass er/sie Kontrolle über Flash/Vib/Sound hat.

### 1.1.1 Flow Overview

1. **App Start (First Run)**
   - Prüfe Flag `hasCompletedOnboarding`.
   - Wenn `false` → zeige Onboarding-Flow.
   - Wenn `true` → direkt Home.

2. **Onboarding Screen 1 – Intro**
   - Inhalt:
     - Titel: “Morse als Rhythmus.”
     - Kurztext: “Übersetze Text ↔ Morse und erlebe ihn als Licht, Vibration, Sound oder visuell.”
   - CTA: `Los geht's` (Primary Button)
   - Secondary: `Überspringen` (Text Button)

3. **Onboarding Screen 2 – Demo (nur visuell / ohne Permissions)**
   - Zeige Text “HI” → `.... ..`
   - Mini-Timeline mit Dot/Dash-Animation (nur visuell, kein Flash/Vibration).
   - CTA: `Verstanden`

4. **Onboarding Screen 3 – Kanäle & Safety**
   - Inhalt:
     - Checkbox-ähnliche Auswahl:
       - [ ] Flash
       - [ ] Vibration
       - [ ] Sound
       - [x] Visuell (Standard aktiv)
     - Hinweis auf Safety:  
       “Flash kann hell und schnell blinken. Du kannst es jederzeit deaktivieren.”
   - Default:
     - Visuell aktiv
     - Alle anderen **aus**, werden später bei Bedarf aktiviert (mit Permissions).
   - CTA: `Starten`

5. **Ende Onboarding**
   - Setze Flag `hasCompletedOnboarding = true`.
   - Navigiere zum Home Screen (Modus: Text → Morse).

### 1.1.2 Edge Cases

- **App killt während Onboarding**  
  - Fortschritt wird minimal persistiert (`onboardingStep`).  
  - Beim Neustart → setze auf letzten vollständigen Screen zurück.
- **User überspringt Onboarding**  
  - Flag wird trotzdem gesetzt, Default-Kanäle: nur Visual aktiv.
- **Accessibility / Screenreader aktiv**  
  - Animationen auf Screen 2 kürzer / weniger hektisch.
  - Keine Auto-Start-Animationen ohne Fokus.

---

## 1.2 Basisflow: Text → Morse

### 1.2.1 Standard Flow

1. User ist auf Home.
2. Modus steht auf **Text → Morse** (Default).
3. User tippt Text in das Input-Feld.
   - Live: Nach jedem Edit wird Morse neu berechnet.
4. Output-Card zeigt:
   - Morse-String
   - Optionale Visualisierung (z.B. Chips pro Buchstabe)
5. User aktiviert gewünschte Ausgabekanäle (Chips).
6. User tippt auf `PLAY`.
7. Playback läuft (siehe 2. Zustandsmodell).
8. User kann:
   - `STOP` drücken
   - Kanäle während des Playbacks togglen (siehe Edge Cases)

### 1.2.2 Edge Cases

- **Leeres Input-Feld**
  - Output bleibt leer.
  - `PLAY`:
    - Disabled oder
    - Tippen zeigt Snackbar: “Kein Inhalt zum Abspielen.”
- **Sehr langer Text**
  - Ab definierter Länge (z.B. 200–300 Zeichen) Warnung:
    - Inline-Hinweis unter Output:  
      “Sehr langer Text – die Wiedergabe kann einige Zeit dauern.”
    - Optional: `Text kürzen`-Action, die nur einen Teil (z.B. ersten Satz) nimmt.
- **Nicht darstellbare Zeichen**
  - Zeichen ohne Morse-Mapping werden:
    - entweder ignoriert oder
    - durch Platzhalter ersetzt.
  - UI zeigt kleine Warnung (Icon + Tooltip):
    - “Einige Zeichen wurden ignoriert.”
- **User editiert Text während Wiedergabe**
  - Optionen (bewusst wählen & dokumentieren):
    1. Playback läuft weiter mit alter Sequenz, Update erst nach Stop.
    2. Playback wird abgebrochen und neu berechnet.
  - Empfehlung für v1:  
    - Playback **stoppen**, Snackbar: “Text geändert – Wiedergabe gestoppt.”

---

## 1.3 Basisflow: Morse → Text

### 1.3.1 Standard Flow (Tap Button)

1. User wählt Modus **Morse → Text** über Segmented Control.
2. Input-Card zeigt großen **Morse-Tap-Button** und ggf. Hinweis:
   - “Tippe kurz für ·, lange für −. Pause = Abstand.”
3. User tippt den Button:
   - App misst Dauer jedes Press.
   - App wandelt in Dot/Dash um (Logik siehe 2.3).
4. Zwischen Taps:
   - Kurze Pause → Zeichen-Trennung (intraChar vs. interChar via Logik).
   - Längere Pause → Buchstaben-/Wort-Gap, wird als `|` / `/` intern markiert.
5. Output-Card zeigt live:
   - Dekodierten Text.
   - Optional: Raw Morse (für Nerds).
6. User kann:
   - Letztes Zeichen löschen (`⌫`).
   - Vollständig löschen.
7. `PLAY` gibt den zuletzt erzeugten Morse-String (aus dem Text) auf Kanälen wieder (optional).

### 1.3.2 Alternative Input: Direkte Morse-Eingabe

Optional (z.B. v1.1+):

- Textfeld, in das User `.- ..` direkt eintippt.
- UI-Validierung:
  - Nur `.` `-` `/` ` ` erlaubt.
  - Ungültige Zeichen → Inline-Error.

### 1.3.3 Edge Cases

- **Unvollständige Sequenz**  
  - Wenn Sequenz kein gültiges Morse-Zeichen ergibt:
    - UI markiert das letzte “unsichere” Segment.
    - Optional: zeigt Vorschläge oder ignoriert es.
- **User tippt extrem unregelmäßig**  
  - Logik muss Toleranzbereiche haben.
  - In Settings: “Morse-Empfindlichkeit” (drei Stufen).

---

## 1.4 Playback Flow (Multimodale Ausgabe)

### 1.4.1 Zustandsmodell Playback

**States**

- `IDLE`
- `PREPARING` (optional, bei langen Strings)
- `PLAYING`
- `PAUSED` (optional v1.1+)
- `ERROR`

**Transitions (High-Level)**

- `IDLE → PREPARING`
  - User tippt `PLAY`
  - Valid Input vorhanden
- `PREPARING → PLAYING`
  - Morse-Sequenz generiert
  - alle aktiven Kanäle bereit
- `PLAYING → IDLE`
  - Sequenz am Ende
  - oder User tippt `STOP`
- `PLAYING → ERROR`
  - Kanal-Fehler (z.B. Flash nicht verfügbar)
- `ERROR → IDLE`
  - Fehler wurde acknowledged (z.B. Snackbar / Dialog)

### 1.4.2 UI Verhalten pro State

| State      | Play/Stop UI                        | Timeline                               | Chips                              |
|------------|-------------------------------------|----------------------------------------|------------------------------------|
| `IDLE`     | Button zeigt `PLAY`                 | Cursor am Anfang, keine Bewegung       | Normal                             |
| `PREPARING`| Button disabled od. Spinner im Button| Timeline leicht ausgegraut            | Chips disabled (kurz)             |
| `PLAYING`  | Button zeigt `STOP`                 | Cursor läuft durch Sequenz             | Aktive Kanäle ggf. pulsierend     |
| `ERROR`    | Button zeigt `PLAY` (wieder)        | Timeline stoppt, ggf. roter Marker     | Problem-Kanal opt. markiert       |

### 1.4.3 Edge Cases

- **Kein aktiver Kanal**
  - User deaktiviert alle Chips und drückt `PLAY`.
  - Verhalten:
    - Option A (empfohlen): Erlaube Wiedergabe, aber nutze immer “Visuell” als Fallback.
    - UI zeigt Hinweis: “Mindestens ein Kanal ist visuell aktiv.”
- **Kanal nicht verfügbar (zur Laufzeit)**  
  Beispiele:
  - Flash belegt durch andere App
  - Vibration vom System deaktiviert
  - Audio-Fokus nicht bekommen
  - Verhalten:
    - Kanal wird für diese Wiedergabe deaktiviert.
    - Snackbar:
      - “Blitz ist momentan nicht verfügbar.” (oder entsprechender Kanal)
    - State bleibt `PLAYING` sofern andere Kanäle laufen.
- **App in Hintergrund / Screen Lock**
  - v1 Empfehlung:
    - Bei `onPause` → Playback stoppen.
    - UI beim Zurückkehren im `IDLE` State, Cursor am Anfang.
  - Hinweis in Spec dokumentieren, ob Hintergrund-Playback bewusst ausgeschlossen ist.

---

## 1.5 History Flow

### 1.5.1 Standard Flow

1. User tippt in App Bar auf `History` (Uhr-Icon).
2. History Screen:
   - Liste, chronologisch absteigend (neueste oben).
   - Einträge zeigen:
     - Quelle (Text / Morse)
     - Datum / Uhrzeit
     - Icon: Richtung (Text → Morse / Morse → Text)
3. User-Aktionen pro Eintrag:
   - Tippen auf ganze Zeile:
     - Navigiert zurück zum Home Screen.
     - Input/Output wird mit diesem Eintrag befüllt.
     - Modus wird entsprechend gesetzt.
   - Kontext-Menü (Overflow):
     - `Favorisieren` (optional)
     - `Teilen`
     - `Löschen`

### 1.5.2 Edge Cases

- **Leere History**
  - Zustand:
    - Illustration + Text:
      - “Noch keine Übersetzungen.”
      - CTA: `Neue Übersetzung starten` → Home
- **Viele Einträge**
  - Lazy-Loading.
  - Optional: Suchfeld / Filter (z.B. nur Text → Morse).
- **Privacy / Clear All**
  - Action im Overflow der App Bar: `Verlauf löschen`.
  - Bestätigung-Dialog:
    - “Verlauf wirklich löschen?”  
      Buttons: `Abbrechen` / `Löschen`.

---

## 1.6 Settings Flow

### 1.6.1 Hauptkategorien

1. **Kanäle**
   - Switch: Flash erlauben
   - Switch: Vibration erlauben
   - Switch: Sound erlauben
   - Switch: Visuelle Darstellung
2. **Morse-Geschwindigkeit**
   - Slider: “Langsam” → “Schnell”
   - Mapped auf die Timing-Tokens (`morse.unit.*`).
3. **Safety**
   - Switch: “Flacker-reduzierter Modus”
   - Beschreibung, was das genau tut.
4. **Design**
   - Option: System-Theme, Hell, Dunkel
   - (Optional) “Mehr Ausdrucksstärke” – toggelt einige Expressive-Motion-Patterns.
5. **Privacy**
   - Switch: Verlauf speichern
   - Button: Verlauf löschen

### 1.6.2 Edge Cases

- **User deaktiviert Verlauf speichern**
  - Ab jetzt:
    - Keine neuen Einträge.
    - Bestehende optional automatisiert löschen (klar dokumentieren).
- **User deaktiviert einen Kanal, der gerade aktiv genutzt wird**
  - Bei Playback:
    - Kanal sofort stoppen.
    - State behalten (andere Kanäle laufen weiter).
- **Morse-Geschwindigkeit extrem niedrig / hoch**
  - Boundaries setzen:
    - Zu langsam → Hinweis: “Sehr langsam – Wiedergabe kann lange dauern.”
    - Zu schnell → Hinweis: “Sehr schnell – möglicherweise schwer zu folgen.”

---

## 1.7 (Optional) Learn / Playground Flow

Kurzfassung:

1. User öffnet “Learn” über Navigation (Icon oder Menü).
2. Screen zeigt Karten:
   - “Alphabet A–M”
   - “Alphabet N–Z”
   - “Zahlen”
3. Tap auf “Alphabet A–M”:
   - Liste (A–M), pro Buchstabe:
     - A — `.-` — Play-Icon
   - Tap auf Zeile → spielt den Buchstaben ab mit aktuellen Kanälen.
4. Optional: Quiz-Modus:
   - App spielt Morse, User soll Buchstabe raten.

Edge Cases:
- Kanäle wie im Home; respektieren Settings/Safety.
- Lernfortschritt kann später persistiert werden (Version > 1.0).

---

# 2. Zustandsmodell & Logik

## 2.1 Global App State (vereinfachtes Modell)

- `mode`: `TEXT_TO_MORSE` | `MORSE_TO_TEXT`
- `inputText`: String
- `inputMorse`: interne Repräsentation (z.B. Sequence von Dots/Dashes/Gaps)
- `outputText`: String
- `outputMorse`: String
- `channels`:
  - `flashEnabled`: bool
  - `vibrationEnabled`: bool
  - `soundEnabled`: bool
  - `visualEnabled`: bool
- `playbackState`: siehe 1.4
- `settings`: Strukturobjekt (Speed, Safety, Theme, etc.)

**Single Source of Truth**:  
Morse-Engine erzeugt aus `inputText` oder `inputMorse` ein **normiertes** internes Format, das für alle Kanäle genutzt wird.

---

## 2.2 Morse-Engine – Normiertes Format

Internes Format (Beispiel):

data class MorseSegment(
  val type: SegmentType, // DOT, DASH, GAP_INTRA, GAP_CHAR, GAP_WORD
  val durationMs: Long
)

- Dot = `DOT` + `morse.unit.dot`
- Dash = `DASH` + `morse.unit.dash`
- Gaps nutzen die `morse.unit.gap.*` Tokens.

Alle Kanäle nutzen **dieselbe Liste von `MorseSegment`**.

---

## 2.3 Morse-Eingabe (Tap-Interpretation)

### 2.3.1 Schwellenwerte

Basierend auf `morse.unit.dot`:

- `dotMax = dot * 1.5`
- `dashMin = dot * 1.5`
- `dashMax = dot * 4` (darüber → ungültig / ignorieren)

Pause-Längen (Time zwischen “Presses”):

- `intraCharMax = gap.intraChar * 1.5`
- `interCharMin = gap.intraChar * 1.5`
- `interCharMax = gap.interChar * 2`
- `wordGapMin = gap.word * 0.8`

Exakte Werte ggf. iterativ mit UX-Tests feinjustieren.

### 2.3.2 Fehlerbehandlung

- Sehr lange Presses → ignorieren + UI-Hinweis:
  - z.B. kurzer Shake-Effekt + Snackbar.
- Sehr unregelmäßige Pausen → lieber tolerant zu Buchstaben-Lücken mappen als sofort Fehler.

---

# 3. Permissions & Safety

## 3.1 Overview

Kanäle & Permissions:

- **Flash**
  - Permission: Kamera/Flash (je nach Android-Version)
  - Sicherheitsaspekte: Photosensitive Users
- **Vibration**
  - Kein explizites Dangerous Permission, aber Device-Fähigkeit checken
- **Sound**
  - Kein Dangerous Permission, aber Audio-Fokus & Lautstärke
- **Visual**
  - Keine Permission, aber Respekt für `reduceMotion` / `reduceAnimations`

## 3.2 Permission Flow: Flash

### 3.2.1 Aktivierung aus Playback Panel

1. User tippt auf Flash-Chip (von OFF → ON).
2. App checkt Permission:
   - Wenn bereits gewährt:
     - Aktiviere Kanal, Chip geht in ON-State.
   - Wenn nicht gewährt:
     1. Zeige **In-App Rationale** (Bottom Sheet):
        - Titel: “Blitz verwenden?”
        - Text:
          - “Damit wir Morse mit Licht anzeigen können, benötigen wir Zugriff auf den Blitz.”
          - Hinweis auf Safety: “Du kannst Flash jederzeit in den Einstellungen ausschalten.”
        - Buttons:
          - `Erlauben` → System-Permissions-Dialog
          - `Abbrechen` → Chip zurück zu OFF
     2. System-Permissions-Dialog.
3. Ergebnis:
   - Wenn Permission gewährt:
     - Chip ON, Flash verfügbar.
   - Wenn abgelehnt:
     - Chip OFF
     - Snackbar: “Ohne Berechtigung kann der Blitz nicht verwendet werden.”

### 3.2.2 Permission “Don’t ask again”

- Wenn System meldet: “Don’t ask again” aktiv:
  - Chip-Tap:
    - Öffne kleines Dialog:
      - “Blitz ist deaktiviert. Du kannst die Berechtigung in den Systemeinstellungen ändern.”
      - Button: `Zu Einstellungen` → OS App Settings
      - Secondary: `Abbrechen`

---

## 3.3 Vibration & Sound

- **Vibration:**
  - Keine Runtime Permission, aber:
    - Vor Nutzung: `hasVibrator()` check.
    - Wenn false:
      - Chip wird disabled, Tooltip: “Dieses Gerät unterstützt keine Vibration.”
- **Sound:**
  - Audio-Fokus:
    - Vor Playback anfordern.
    - Bei Verlust:
      - Playback stoppen.
      - Snackbar: “Sound wurde von einer anderen App unterbrochen.”

---

## 3.4 Safety-Modus (Flash)

- Wenn `safety.flashReduced` aktiv:
  - Min-Gap zwischen Blinkern erzwingen.
  - Evtl. Helligkeit leicht reduzieren (Device-abhängig).
- Bei Änderung dieser Einstellung:
  - Kurztext anzeigen:
    - “Der flacker-reduzierte Modus verringert schnelle Blinkmuster.”

---

# 4. Fehlerzustände & UX

## 4.1 Fehlerklassen

1. **User-Input-Fehler**
   - Ungültiges Morse
   - Leeres Input
2. **System-/Device-Fehler**
   - Keine Vibration
   - Flash nicht verfügbar
   - Kein Audio-Fokus
3. **App-interne Fehler**
   - Morse-Engine unerwartet (Edge Cases)
   - JSON/History DB kaputt

## 4.2 Darstellung & Pattern

### 4.2.1 User-Input-Fehler

- Inline-Fehlermeldung **bevor** Snackbar/Dialog.
- Beispiele:
  - Morse-Eingabe enthält unerlaubte Zeichen → roter Text unter Feld:
    - “Ungültiges Zeichen: nur . - / erlaubt.”
  - Keine Eingabe bei Play:
    - Button disabled (Primärlösung)
    - Optional Snackbar beim Tap (falls nicht disabled):  
      “Kein Inhalt zum Abspielen.”

### 4.2.2 System-/Device-Fehler

- Nach Möglichkeit **non-blocking**:
  - Snackbar mit klarer Ursache + Kanal.
- Beispiele:
  - Flash:
    - “Der Blitz ist aktuell nicht verfügbar.”
  - Vibration nicht verfügbar:
    - Chip wird disabled, Tooltip oder ein einmaliger Dialog.

### 4.2.3 Interne Fehler

- Fallback:
  - Snackbar: “Es ist ein unerwarteter Fehler aufgetreten.”
  - Logging: intern / Crashlytics, aber **kein** UI-Leak sensibler Infos.
- Playback:
  - State → `ERROR` → UI zurück auf `IDLE`, Buttons wieder aktiv.

---

# 5. Persistenz & Privacy

## 5.1 History

- Speicherort:
  - Lokal, verschlüsselt (wenn machbar; abhängig von App-Scope).
- Inhalte:
  - `id`
  - `timestamp`
  - `direction` (Text→Morse / Morse→Text)
  - `inputText` (falls Text)
  - `outputText`
  - `outputMorse`
- Default:
  - Verlauf **an** (configurierbar).
- Max-Einträge:
  - Z.B. 500; älteste Einträge löschen (Rolling).

## 5.2 Settings

- Lokal, per SharedPreferences o.ä.
- Einstellungen:
  - Kanäle
  - Speed
  - Safety
  - Theme
  - Verlauf-Speicher-Flag
- Kein automatisches Syncing in Cloud (v1).

---

# 6. Testing / Edge Case Checkliste

Kurz-Checkliste für QA / Design-Review:

1. **First Run**
   - Onboarding wird genau einmal gezeigt.
   - Onboarding kann sauber geskippt werden.
2. **Text → Morse**
   - Leeres Input: `PLAY` disabled.
   - Lange Inputs: UI bleibt performant, Hinweis vorhanden.
   - Spezielle Zeichen: definierte Strategie eingehalten.
3. **Morse → Text**
   - Tap-Eingabe fühlt sich tolerant, aber konsistent an.
   - Unklare Taps resultieren nicht in “kaputter” UI.
4. **Playback**
   - Statewechsel sind visuell klar.
   - Unterschiedliche Kombinationen der Kanäle funktionieren.
5. **Permissions**
   - Flash-Flow funktioniert bei:
     - First Request
     - Deny
     - Deny + Don’t ask again
   - Verhalten, wenn Device keine Vibration hat.
6. **Settings**
   - Speed-Slider beeinflusst wirklich alle Kanäle gleich.
   - Safety-Modus deutlich spürbar.
7. **Accessibility**
   - Screenreader-Texte sinnvoll.
   - Große Schriftgrößen brechen Layout nicht.
   - Reduce Motion respektiert: weniger/keine aufwendigen Timeline-Animationen.

---

# 7. Changelog

- **1.0.0 (2025-11-16)**
  - Erste Version des Flow- und Edge-Case-Dokuments
  - Deckt First-Run, Text→Morse, Morse→Text, Playback, History, Settings, Permissions & Fehler ab.
