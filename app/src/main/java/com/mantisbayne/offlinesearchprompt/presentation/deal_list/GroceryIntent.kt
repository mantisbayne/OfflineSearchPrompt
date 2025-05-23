package com.mantisbayne.offlinesearchprompt.presentation.deal_list

import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.DealDisplayable
import com.mantisbayne.offlinesearchprompt.presentation.deal_list.model.FilterItem

// handle loading toggling filtering
sealed class GroceryIntent {
    data object LoadData : GroceryIntent()
    data class ToggleFilter(val filter: FilterItem) : GroceryIntent()
    data class ToggleExpandDeal(val deal: DealDisplayable) : GroceryIntent()
    data class Search(val query: String) : GroceryIntent()
}