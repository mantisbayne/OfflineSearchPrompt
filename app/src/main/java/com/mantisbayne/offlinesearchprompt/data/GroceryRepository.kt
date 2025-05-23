package com.mantisbayne.offlinesearchprompt.data

import android.content.Context
import kotlinx.serialization.json.Json

interface GroceryRepository {
    fun getDeals(): List<GroceryItem>
    fun getFilters(): List<String>
}

class GroceryRepositoryImpl(private val context: Context) : GroceryRepository {
    override fun getDeals(): List<GroceryItem> {
        return deserialize("items.json")
    }

    override fun getFilters(): List<String> {
        return deserialize("categories.json")
    }

    private inline fun <reified T> deserialize(fileName: String): T {
        val jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        return Json.decodeFromString(jsonString)
    }
}
