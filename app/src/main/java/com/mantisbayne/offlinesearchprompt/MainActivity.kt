package com.mantisbayne.offlinesearchprompt

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mantisbayne.offlinesearchprompt.data.GroceryRepositoryImpl
import com.mantisbayne.offlinesearchprompt.mvi.DealPillDisplayable
import com.mantisbayne.offlinesearchprompt.mvi.FilterItem
import com.mantisbayne.offlinesearchprompt.mvi.GroceryDisplayable
import com.mantisbayne.offlinesearchprompt.mvi.GroceryIntent
import com.mantisbayne.offlinesearchprompt.ui.theme.OfflineSearchPromptTheme
import com.mantisbayne.offlinesearchprompt.mvi.GroceryViewModel
import com.mantisbayne.offlinesearchprompt.mvi.GroceryViewModelFactory
import com.mantisbayne.offlinesearchprompt.mvi.GroceryViewState
import com.mantisbayne.offlinesearchprompt.mvi.PillType
import com.mantisbayne.offlinesearchprompt.ui.theme.components.CardComponent
import com.mantisbayne.offlinesearchprompt.ui.theme.components.PillComponent
import kotlinx.coroutines.delay

/*
🧩 Requirements:
List UI:

Use Jetpack Compose to display a vertically scrolling list of deals.

Each item should show the product image, name, price, discount badge (if applicable), and deal expiration.

Visually distinguish expired deals (e.g., grayed out or with a strikethrough).

Data:

Use a mock API or static JSON file (e.g., in assets) and parse it using kotlinx.serialization or Moshi.

Assume deal expiration is a string like "2025-05-25T23:59:59Z" and handle parsing with a suitable date lib.

Bonus (if time):

Add a filter toggle to show only active deals.

Display a badge if the discount is >20% ("HOT DEAL").
 */

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OfflineSearchPromptTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val context = LocalContext.current
                    val repository = remember { GroceryRepositoryImpl(context) }
                    val viewModel: GroceryViewModel = viewModel(
                        factory = GroceryViewModelFactory(repository)
                    )
                    val viewState by viewModel.viewState.collectAsState()

                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.TopCenter
                    ) {

                        when {
                            viewState.loading -> Text("Loading...")
                            !viewState.error.isNullOrBlank() -> Text(viewState.error ?: "Error")
                            else -> GroceryScreen(
                                viewState,
                                viewModel::dispatch
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GroceryScreen(
    viewState: GroceryViewState,
    onIntent: (GroceryIntent) -> Unit
) {
    var query by remember { mutableStateOf("") }

    LaunchedEffect(query) {
        delay(300)
        onIntent(GroceryIntent.Search(query))
    }

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        SearchField(query, { query = it}, { query = "" })
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(viewState.filters) { filterItem ->
                FilterItem(filterItem, onIntent)
            }
        }
        GroceryItemList(viewState.items)
    }
}

@Composable
private fun SearchField(
    query: String,
    onValueChange: (String) -> Unit,
    onClearClicked: () -> Unit
) {
    TextField(
        value = query,
        onValueChange = onValueChange,
        placeholder = { Text("Search deals…") },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search deals"
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = onClearClicked
                ) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "clear"
                    )
                }
            }
        },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    )
}

@Composable
fun FilterItem(
    filterItem: FilterItem,
    onIntent: (GroceryIntent) -> Unit
) {

    PillComponent(
        modifier = Modifier.padding(end = 8.dp, start = 8.dp),
        color = if (filterItem.isSelected) Color(0xFF2E7D32) else Color.LightGray,
        isClickable = true,
        cornerShape = RoundedCornerShape(16.dp),
        onClick = { onIntent(GroceryIntent.ToggleFilter(filterItem)) }
    ) {
        Text(
            text = filterItem.label,
            style = MaterialTheme.typography.labelSmall,
            color = if (filterItem.isSelected) Color.White else Color.DarkGray
        )
    }
}

@Composable
fun GroceryItemList(
    items: List<GroceryDisplayable>
) {
    LazyColumn(
        Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { groceryItem ->
            DealCard(groceryItem)
        }
    }
}

@Composable
private fun DealCard(groceryItem: GroceryDisplayable) {
    CardComponent {
        DealCardContent(groceryItem)
    }
}

@Composable
private fun DealCardContent(groceryItem: GroceryDisplayable) {
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
        Text(
            text = groceryItem.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = groceryItem.description,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = groceryItem.expiresInText,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Gray
        )
        DealPillList(groceryItem.deals)
        Button(
            colors = ButtonDefaults.buttonColors(Color(0xFF2E7D32)),
            onClick = {}
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "View Deal",
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun DealPillList(items: List<DealPillDisplayable>) {
    LazyRow(
        modifier = Modifier
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(items) { item ->
            when (item.pillType) {
                is PillType.HotDeal -> DealPill("Hot Deal", Color.Red)
                is PillType.Expired -> DealPill("Expired", Color.LightGray)
                is PillType.Other -> DealPill(item.pillType.infoText, Color(0xFFFFF176))
            }
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

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    val pillTypes = listOf(
        PillType.HotDeal,
        PillType.Expired,
        PillType.Other("10% off")
    )
    val image = R.drawable.hotdog
    val deals = listOf(
        DealPillDisplayable(PillType.HotDeal),
        DealPillDisplayable(PillType.Expired),
        DealPillDisplayable(PillType.Other("Expired in 10 days"))
    )
    val items = listOf(
        GroceryDisplayable(
            name = "Banana",
            price = "1.99",
            image = image,
            description = "This is a banana on sale",
            expiresInText = "Expires in 3 days",
            deals = deals,
            category = "Fruit"
        ),
        GroceryDisplayable(
            "Cheese",
            "8.15",
            image,
            "This is some cheese on sale from the North of France. Gale the cook grew up there, where he learned all about specialized French cheeses",
            "10 days",
            deals,
            category = "Fruit"
        ),
        GroceryDisplayable(
            "Milk",
            "3.99",
            image,
            "This is a banana on sale",
            "33 days",
            deals,
            "Fruit"
        ),
    )
    val viewState = GroceryViewState(
        items = items,
        filters = listOf(
            FilterItem("Banana", true),
            FilterItem("Cheese", false),
            FilterItem("Milk", false)
        )
    )
    GroceryScreen(viewState, {})
}