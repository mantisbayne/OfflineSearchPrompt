package com.mantisbayne.offlinesearchprompt

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mantisbayne.offlinesearchprompt.data.GroceryRepositoryImpl
import com.mantisbayne.offlinesearchprompt.ui.theme.OfflineSearchPromptTheme
import com.mantisbayne.offlinesearchprompt.viewmodel.GroceryDisplayable
import com.mantisbayne.offlinesearchprompt.viewmodel.GroceryViewModel
import com.mantisbayne.offlinesearchprompt.viewmodel.GroceryViewModelFactory

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
                    val items by viewModel.items.collectAsState()

                    GroceryScreen(
                        modifier = Modifier.padding(innerPadding),
                        items = items
                    )
                }
            }
        }
    }
}

@Composable
fun GroceryScreen(
    modifier: Modifier,
    items: List<GroceryDisplayable>
) {

    LazyColumn(modifier) {
        items(items) {groceryItem ->
            GroceryItem(groceryItem = groceryItem)
        }
    }
}

@Composable
fun GroceryItem(
    groceryItem: GroceryDisplayable
) {

    Card(
        modifier = Modifier
            .background(if (groceryItem.isHotDeal) Color.Red else Color.Magenta)
            .padding(16.dp),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            Image(
                modifier = Modifier.size(96.dp),
                painter = painterResource(R.drawable.ic_launcher_background),
                contentDescription = "item image"
            )
            Row(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = groceryItem.name,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = groceryItem.price,
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Row {
                Text(
                    text = groceryItem.expiresInText,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {

}