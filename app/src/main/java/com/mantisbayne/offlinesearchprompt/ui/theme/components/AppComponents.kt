package com.mantisbayne.offlinesearchprompt.ui.theme.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.unit.dp

@Composable
fun CardComponent(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(Color.White),
        elevation = CardDefaults.cardElevation(10.dp)
    ) {
        content()
    }
}

@Composable
fun PillComponent(
    color: Color,
    cornerShape: RoundedCornerShape,
    modifier: Modifier = Modifier,
    isClickable: Boolean = false,
    onClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    val clickableModifier = if (isClickable) {
        modifier
            .background(color, shape = cornerShape)
            .clickable { onClick() }
    } else {
        modifier.background(color, shape = cornerShape)
    }

    Box(
        modifier = clickableModifier
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}
