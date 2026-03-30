package com.sukajee.core.design.utils

import androidx.compose.ui.graphics.Color
import androidx.core.graphics.toColorInt


fun hexToColor(hex: String): Color {
    val hexWithoutHash = hex.removePrefix("#")
    return Color("#$hexWithoutHash".toColorInt())
}
