package com.mantisbayne.offlinesearchprompt.presentation.deal_list.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.DealPillDisplayable
import com.mantisbayne.offlinesearchprompt.ui.theme.components.PillComponent

@Composable
fun DealPillList(items: List<DealPillDisplayable>) {
    LazyRow(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            DealPill(item.text, item.backgroundColor)
        }
    }
}

@Composable
fun DealPill(text: String, color: Color) {
    PillComponent(color, RoundedCornerShape(25.dp)) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = if (color == Color.Red) Color.White else Color.DarkGray
        )
    }
}
