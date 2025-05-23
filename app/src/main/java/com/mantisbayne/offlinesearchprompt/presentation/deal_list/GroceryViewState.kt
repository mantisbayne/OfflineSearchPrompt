package com.mantisbayne.offlinesearchprompt.presentation.deal_list

import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.DealDisplayable
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.FilterItem

// handle loading, error, items, filtering, search query
data class GroceryViewState(
    val loading: Boolean = false,
    val error: String? = null,
    val items: List<DealDisplayable> = emptyList(),
    val allItems: List<DealDisplayable> = emptyList(),
    val showOnlyActive: Boolean = false,
    val query: String = "",
    val filters: List<FilterItem> = emptyList()
)