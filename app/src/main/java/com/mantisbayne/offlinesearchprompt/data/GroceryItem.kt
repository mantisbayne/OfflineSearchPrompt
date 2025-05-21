package com.mantisbayne.offlinesearchprompt.data

import kotlinx.serialization.Serializable

@Serializable
data class GroceryItem(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    val discountPercent: Int = 0,
    val imageUrl: String = "",
    val description: String = "",
    val expirationDate: String = "",
    val category: String = ""
)
