package com.mantisbayne.offlinesearchprompt.mvi

// handle loading, error, items, filtering, search query
data class GroceryViewState(
    val loading: Boolean = false,
    val error: String? = null,
    val items: List<GroceryDisplayable> = emptyList(),
    val allItems: List<GroceryDisplayable> = emptyList(),
    val showOnlyActive: Boolean = false,
    val query: String = "",
    val filters: List<FilterItem> = emptyList()
)