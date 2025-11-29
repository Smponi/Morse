package com.mouse.mouse.ui.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Design Token System für konsistente Größen und Abstände
 * 
 * Diese Datei enthält alle wiederverwendbaren Größendefinitionen für die App.
 * Verwende diese Tokens statt hardcodierte Werte für bessere Wartbarkeit.
 */
object AppDimensions {
    
    // ==================== SPACING (Abstände) ====================
    object Spacing {
        val xxSmall: Dp = 4.dp      // Sehr kleine Abstände zwischen eng verbundenen Elementen
        val xSmall: Dp = 8.dp       // Kleine Abstände innerhalb von Components
        val small: Dp = 12.dp       // Standard kleine Abstände
        val medium: Dp = 16.dp      // Standard Medium Abstände (meistverwendet)
        val large: Dp = 24.dp       // Große Abstände zwischen Sections
        val xLarge: Dp = 32.dp      // Sehr große Abstände
        val xxLarge: Dp = 48.dp     // Extra große Abstände für wichtige Separations
    }
    
    // ==================== COMPONENT HEIGHTS ====================
    object Height {
        val buttonSmall: Dp = 48.dp         // Kleine Action Buttons
        val buttonMedium: Dp = 56.dp        // Standard Buttons (z.B. Transmit)
        val buttonLarge: Dp = 64.dp         // Große wichtige Buttons
        
        val keyboardRow: Dp = 64.dp         // Utility Row im Morse Keyboard
        val telegraphPad: Dp = 160.dp       // Die großen Dot/Dash Pads
        
        val inputCardMinMorse: Dp = 180.dp  // Mindesthöhe für Morse Terminal
        val inputCardMinText: Dp = 0.dp     // Text Input passt sich an
        
        val outputCardMin: Dp = 80.dp       // Minimale Höhe für Output Display
        val visualizer: Dp = 100.dp         // Signal Visualizer Größe
    }
    
    // ==================== CORNER RADIUS ====================
    object CornerRadius {
        val small: Dp = 12.dp       // Kleine Elemente (Chips, kleine Buttons)
        val medium: Dp = 16.dp      // Standard Buttons, Cards
        val large: Dp = 24.dp       // Große Cards, Input Fields
        val xLarge: Dp = 28.dp      // Telegraph Pads
        val round: Dp = 50.dp       // Komplett runde Elemente (Pills)
    }
    
    // ==================== BORDER WIDTH ====================
    object Border {
        val thin: Dp = 1.dp         // Standard Border
        val medium: Dp = 2.dp       // Hervorgehobene Border
        val thick: Dp = 4.dp        // Dicke Border für Emphasis
    }
    
    // ==================== ICON SIZES ====================
    object IconSize {
        val small: Dp = 18.dp       // Kleine Icons in Chips
        val medium: Dp = 24.dp      // Standard Icons
        val large: Dp = 32.dp       // Große Icons (z.B. in Visualizer)
        val xLarge: Dp = 40.dp      // Sehr große Icons
    }
    
    // ==================== PADDING ====================
    object Padding {
        val cardSmall: Dp = 16.dp       // Kleine Cards
        val cardMedium: Dp = 20.dp      // Standard Cards
        val cardLarge: Dp = 24.dp       // Große Cards
        
        val screenHorizontal: Dp = 24.dp    // Screen Seiten-Padding
        val screenVertical: Dp = 16.dp      // Screen Oben/Unten-Padding
    }
    
    // ==================== TYPOGRAPHY (Letter Spacing) ====================
    object LetterSpacing {
        val tight = 0.sp            // Kein extra spacing
        val normal = 1.sp           // Standard spacing
        val medium = 2.sp           // Medium spacing (Labels)
        val wide = 4.sp             // Breites spacing (Morse Code)
    }
}
