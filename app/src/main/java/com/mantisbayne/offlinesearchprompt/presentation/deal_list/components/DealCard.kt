package com.mantisbayne.offlinesearchprompt.presentation.deal_list.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mantisbayne.offlinesearchprompt.R
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.DealDisplayable
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.DealPillDisplayable
import com.mantisbayne.offlinesearchprompt.ui.theme.components.CardComponent

@Composable
fun DealCard(
    groceryItem: DealDisplayable,
    onClick: () -> Unit
) {
    CardComponent(
        modifier = Modifier
            .clickable { onClick() }
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearOutSlowInEasing
                )
            )
    ) {
        DealCardContent(
            groceryItem
        )
    }
}

@Composable
private fun DealCardContent(
    groceryItem: DealDisplayable
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
        ) {
            Image(
                modifier = Modifier
                    .fillMaxSize(),
                painter = painterResource(R.drawable.hotdog),
                contentDescription = "${groceryItem.name} image"
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier =
                    Modifier.weight(1f),
                text = groceryItem.name,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = groceryItem.expiresInText,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }
        if (groceryItem.isExpanded) {
            DealCardDetails(
                groceryItem
            )
        }
    }
}

@Composable
private fun DealCardDetails(
    groceryItem: DealDisplayable
) {
    DealPillList(groceryItem.deals)
    Text(
        text = groceryItem.description,
        style = MaterialTheme.typography.bodySmall,
        textAlign = TextAlign.Center
    )
    Spacer(Modifier.height(8.dp))
    AnimatedVisibility(
        visible = groceryItem.isExpanded,
        modifier = Modifier.fillMaxWidth(),
        enter = fadeIn(initialAlpha = 0.3f),
        exit = fadeOut()
    ) {
        Button(
            colors = ButtonDefaults.buttonColors(Color(0xFF2E7D32)),
            onClick = {}
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.button_buy),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val image = R.drawable.hotdog
    val deals = listOf(
        DealPillDisplayable("Hot Deal", Color.Red),
        DealPillDisplayable("Expired", Color.LightGray),
        DealPillDisplayable("10% off", Color.Green)
    )

    Column {
        DealCard(
            groceryItem = DealDisplayable(
                "Milk",
                "3.99",
                image,
                "This is a banana on saleThis is a banana on saleThis is a banana on saleThis is a banana on saleThis is a banana on saleThis is a banana on sale",
                "33 days",
                deals,
                "Fruit",
                true
            ), onClick = {})
        DealCard(
            groceryItem = DealDisplayable(
                "Milk",
                "3.99",
                image,
                "This is a banana on saleThis is a banana on saleThis is a banana on saleThis is a banana on saleThis is a banana on saleThis is a banana on sale",
                "33 days",
                deals,
                "Fruit"
            ), onClick = {})
    }
}
