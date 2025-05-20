package com.mantisbayne.offlinesearchprompt.data

import android.content.Context
import kotlinx.serialization.json.Json

interface GroceryRepository {
    fun getDeals(): List<GroceryItem>
}

class GroceryRepositoryImpl(private val context: Context) : GroceryRepository {
    override fun getDeals(): List<GroceryItem> {
        val json = context.assets.open("items.json").bufferedReader().use { it.readText() }
        return Json.decodeFromString(json)
    }
}
