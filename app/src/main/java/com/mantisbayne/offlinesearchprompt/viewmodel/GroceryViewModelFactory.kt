package com.mantisbayne.offlinesearchprompt.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mantisbayne.offlinesearchprompt.data.GroceryRepository

class GroceryViewModelFactory(private val repository: GroceryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroceryViewModel::class.java)) {
            return GroceryViewModel(repository) as T
        }
        throw IllegalStateException("Unknown ViewModel")
    }
}