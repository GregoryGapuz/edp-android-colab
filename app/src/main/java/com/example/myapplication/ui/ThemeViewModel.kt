package com.example.myapplication.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.data.ThemeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ThemeViewModel(private val repo: ThemeRepository) : ViewModel() {

    val isDarkTheme: StateFlow<Boolean> = repo.isDarkTheme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), false)

    fun onThemeChanged(dark: Boolean) {
        viewModelScope.launch { repo.setDarkTheme(dark) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as android.app.Application).applicationContext
                ThemeViewModel(ThemeRepository(context))
            }
        }
    }
}
