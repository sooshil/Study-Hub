package com.sukajee.core.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import com.sukajee.core.design.theme.Indigo500
import com.sukajee.core.design.theme.Purple500

@Composable
fun GradientHeaderBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier = modifier.background(
            brush = Brush.linearGradient(
                colors = listOf(Indigo500, Purple500)
            )
        ),
        content = content
    )
}
