package com.mantisbayne.offlinesearchprompt.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mantisbayne.offlinesearchprompt.data.GroceryItem
import com.mantisbayne.offlinesearchprompt.data.GroceryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class GroceryViewModel(
    private val repository: GroceryRepository
) : ViewModel() {

    private val _items = MutableStateFlow<List<GroceryDisplayable>>(emptyList())
    val items: StateFlow<List<GroceryDisplayable>> = _items.asStateFlow()

    init {
        getGroceryList()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getGroceryList() {
        viewModelScope.launch {
            val response = repository.getDeals()
            _items.value = response.map {
                it.toDisplayable()
            }
        }
    }
}

data class GroceryDisplayable(
    val name: String,
    val price: String,
    val discountPercent: Int,
    val imageUrl: String,
    val isExpired: Boolean,
    val isHotDeal: Boolean,
    val expiresInText: String
)

@RequiresApi(Build.VERSION_CODES.O)
fun GroceryItem.toDisplayable(): GroceryDisplayable {
    val expiration = ZonedDateTime.parse(expirationDate)
    val now = ZonedDateTime.now()

    val isExpired = expiration.isBefore(now)
    val isHotDeal = discountPercent >= 20
    val expiresInDays = java.time.Duration.between(now, expiration).toDays()

    val expiresInText = if (isExpired) {
        "Expired"
    } else {
        "Ends in $expiresInDays day${if (expiresInDays != 1L) "s" else ""}"
    }
    return GroceryDisplayable(
        name,
        price,
        discountPercent,
        imageUrl,
        isExpired,
        isHotDeal,
        expiresInText
    )
}