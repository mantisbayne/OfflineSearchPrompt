package com.mantisbayne.offlinesearchprompt.presentation.deal_list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mantisbayne.offlinesearchprompt.data.GroceryRepository

class GroceryViewModelFactory(private val repository: GroceryRepository) :
    ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GroceryViewModel::class.java)) {
            return GroceryViewModel(repository) as T
        }
        throw IllegalStateException("Unknown ViewModel")
    }
}