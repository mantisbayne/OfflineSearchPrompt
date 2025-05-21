package com.mantisbayne.offlinesearchprompt.mvi

// handle loading toggling filtering
sealed class GroceryIntent {
    data object LoadData : GroceryIntent()
    data class ToggleFilter(val filter: FilterItem) : GroceryIntent()
    data class Search(val query: String) : GroceryIntent()
}