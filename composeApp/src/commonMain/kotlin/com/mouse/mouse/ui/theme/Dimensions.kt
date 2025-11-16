package com.mouse.mouse.ui.theme

import androidx.compose.ui.unit.dp

// Spacing Tokens
object Spacing {
    val zero = 0.dp
    val xs = 4.dp
    val sm = 8.dp
    val md = 12.dp
    val lg = 16.dp
    val xl = 24.dp
    val xxl = 32.dp
}

// Layout Tokens
object Layout {
    val screenPaddingHorizontal = 16.dp
    val screenPaddingVertical = 16.dp
    val cardPadding = 16.dp
    val chipSpacing = 8.dp
    val sectionGap = 24.dp
}

// Radius Tokens
object Radius {
    val xs = 4.dp
    val sm = 8.dp
    val lg = 16.dp
    val pill = 999.dp
}

// Elevation Tokens
object Elevation {
    val none = 0.dp
    val card = 1.dp
    val cardRaised = 3.dp
    val bottomPanel = 4.dp
    val fab = 6.dp
}

// Icon & Control Sizes
object Size {
    object Icon {
        val sm = 18.dp
        val md = 24.dp
        val lg = 32.dp
    }

    object Control {
        val sm = 32.dp
        val md = 40.dp
        val lg = 56.dp
    }
}

// Motion & Timing (in milliseconds)
object MotionDuration {
    const val xs = 100
    const val sm = 150
    const val md = 220
    const val lg = 300
}

// Morse-specific Timing (in milliseconds) - Defaults
object MorseTiming {
    const val dot = 120L
    const val dash = 360L
    const val gapIntraChar = 120L
    const val gapInterChar = 360L
    const val gapWord = 840L
}
