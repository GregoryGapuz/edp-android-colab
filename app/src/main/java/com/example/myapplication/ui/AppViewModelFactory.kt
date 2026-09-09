package com.example.myapplication.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.*

class AppViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = AppDatabase.get(context)
        @Suppress("UNCHECKED_CAST")
        return when {
            modelClass.isAssignableFrom(PostsViewModel::class.java) ->
                PostsViewModel(PostRepository(db.postDao())) as T
            modelClass.isAssignableFrom(ThemeViewModel::class.java) ->
                ThemeViewModel(ThemeRepository(context)) as T
            else -> throw IllegalArgumentException("Unknown ViewModel")
        }
    }
}