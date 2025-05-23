package com.mantisbayne.offlinesearchprompt.mvi

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mantisbayne.offlinesearchprompt.data.GroceryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            is GroceryIntent.Search -> performSearch(intent.query)
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _viewState.update { viewState ->
                val selectedCategories: List<FilterItem> = viewState.filters.filter { it.isSelected }
                val items = applyFilters(viewState.allItems, selectedCategories, query)

                viewState.copy(
                    items = items,
                    query = query
                )
            }
        }
    }

    private fun applyFilters(
        allItems: List<GroceryDisplayable>,
        selectedCategories: List<FilterItem>,
        query: String
    ): List<GroceryDisplayable> {
        val selectedLabels = selectedCategories.map { it.label }
        return allItems
            .filter { selectedLabels.isEmpty() || it.category in selectedLabels }
            .filter { query.isBlank() || it.name.contains(query, ignoreCase = true) }
    }

    private fun loadData() {
        viewModelScope.launch {
            _viewState.value = _viewState.value.copy(loading = true)

            try {
                val (filters, deals) = withContext(Dispatchers.IO) {

                    val filters = repository.getFilters()
                    val deals = repository.getDeals()

                    filters to deals
                }
                _viewState.update { viewState ->
                    val allItems = deals.map { it.toDisplayable() }
                    val existing = viewState.filters.associateBy { it.label }
                    val newFilters = filters.map { label ->
                        existing[label] ?: FilterItem(label)
                    }

                    GroceryViewState(
                        loading = false,
                        allItems = allItems,
                        items = applyFilters(
                            allItems,
                            newFilters,
                            viewState.query
                        )
                    )
                }
            } catch (e: Exception) {
                _viewState.value = _viewState.value.copy(
                    loading = false,
                    error = e.message
                )
            }
        }
    }

    private fun toggleFilter(filterItem: FilterItem) {
        _viewState.update { viewState ->
            val updatedFilters = viewState.filters.map {
                if (it.label == filterItem.label) it.copy(isSelected = !it.isSelected) else it
            }
            val selectedFilters = updatedFilters.filter { it.isSelected }
            val filteredItems = applyFilters(viewState.allItems, selectedFilters, viewState.query)

            viewState.copy(
                filters = updatedFilters,
                items = filteredItems
            )
        }
    }
}
