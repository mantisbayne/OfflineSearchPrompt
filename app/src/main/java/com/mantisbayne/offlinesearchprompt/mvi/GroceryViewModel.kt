package com.mantisbayne.offlinesearchprompt.mvi

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mantisbayne.offlinesearchprompt.R
import com.mantisbayne.offlinesearchprompt.data.GroceryItem
import com.mantisbayne.offlinesearchprompt.data.GroceryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
class GroceryViewModel(
    private val repository: GroceryRepository
) : ViewModel() {

    private val _viewState = MutableStateFlow<GroceryViewState>(GroceryViewState())
    val viewState: StateFlow<GroceryViewState> = _viewState.asStateFlow()

    init {
        dispatch(GroceryIntent.LoadData)
    }

    fun dispatch(intent: GroceryIntent) {
        when (intent) {
            is GroceryIntent.LoadData -> loadData()
            is GroceryIntent.ToggleFilter -> toggleFilter(intent.filter)
            is GroceryIntent.Search -> {}
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(loading = true)

            try {
                val items = repository.getDeals()
                val categories = repository.getCategories()
                val filters = categories.map { FilterItem(it) }
                val groceryItems = items.map { it.toDisplayable() }
                val filteredItems = groceryItems.filter { it.category == filters.first().label }
                _viewState.value = _viewState.value.copy(
                    loading = false,
                    items = filteredItems,
                    allItems = groceryItems,
                    filters = filters
                )
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    private fun toggleFilter(filterItem: FilterItem) {
        viewModelScope.launch {
            _viewState.update { viewState ->
                val newFilters = viewState.filters.map {
                    if (it.label == filterItem.label) {
                        it.copy(isSelected = !it.isSelected)
                    } else {
                        it
                    }
                }
                val selectedCategories = newFilters.filter { it.isSelected }.map { it.label }

                val filteredItems = viewState.allItems.filter { item ->
                    selectedCategories.isEmpty() || item.category in selectedCategories
                }
                viewState.copy(filters = newFilters, items = filteredItems)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun GroceryItem.toDisplayable(): DisplayableModels {
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
    val image = R.drawable.hotdog
    val deals = listOf(
        DealPillDisplayable(PillType.HotDeal),
        DealPillDisplayable(PillType.Expired),
        DealPillDisplayable(PillType.Other("$discountPercent% Off"))
    )
    return DisplayableModels(
        name,
        price,
        image,
        description,
        expiresInText,
        deals,
        category
    )
}