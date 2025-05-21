package com.mantisbayne.offlinesearchprompt.mvi

import androidx.annotation.DrawableRes

data class DisplayableModels(
    val name: String,
    val price: String,
    @DrawableRes
    val image: Int,
    val description: String,
    val expiresInText: String,
    val deals: List<DealPillDisplayable>,
    val category: String = ""
)

data class DealPillDisplayable(
    val pillType: PillType
)

sealed class PillType {
    data object HotDeal : PillType()
    data class Other(val infoText: String = "") : PillType()
    data object Expired : PillType()
}

data class FilterItem(
    val label: String = "",
    val isSelected: Boolean = false
)
