package com.example.myapplication.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.myapplication.core.AppResult
import com.example.myapplication.data.network.NetworkModule
import com.example.myapplication.data.repository.ChatRepositoryImpl
import com.example.myapplication.domain.ChatRepository
import kotlinx.coroutines.launch

class ChatViewModel(
    private val repository: ChatRepository
) : ViewModel() {
    var uiState: ChatUiState by mutableStateOf(ChatUiState.Loading)
        private set
    var myName: String by mutableStateOf("")
        private set
    var draft: String by mutableStateOf("")
        private set

    fun onNameChange(value: String) { myName = value }
    fun onDraftChange(value: String) { draft = value }

    init { 
        load()
        startAutoRefresh()
    }

    private fun startAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(10_000)
                // Only auto-refresh if we're not currently in an error state from a manual action
                if (uiState !is ChatUiState.Error) {
                    repository.getMessages().let { r ->
                        if (r is AppResult.Success) {
                            uiState = if (r.data.isEmpty()) ChatUiState.Empty else ChatUiState.Ready(r.data)
                        }
                    }
                }
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            uiState = ChatUiState.Loading
            uiState = when (val r = repository.getMessages()) {
                is AppResult.Success -> if (r.data.isEmpty()) ChatUiState.Empty else ChatUiState.Ready(r.data)
                AppResult.Failure.NoInternet -> ChatUiState.Error("No internet connection.")
                AppResult.Failure.Timeout -> ChatUiState.Error("The server took too long.")
                is AppResult.Failure -> ChatUiState.Error("Something went wrong.")
            }
        }
    }

    fun send() {
        if (myName.isBlank() || draft.isBlank()) return
        viewModelScope.launch {
            val result = repository.sendMessage(myName, draft)
            when (result) {
                is AppResult.Success -> {
                    draft = ""
                    load()
                }
                is AppResult.Failure -> {
                    val msg = when(result) {
                        AppResult.Failure.NoInternet -> "No internet connection."
                        AppResult.Failure.Timeout -> "The server took too long."
                        is AppResult.Failure.Unknown -> {
                            val original = result.message ?: "Unknown error."
                            if (original.contains("Max number of elements")) {
                                // AUTO-FIX: The server is full, try clearing some space
                                viewModelScope.launch {
                                    repository.clearOldMessages()
                                    // Try sending again or just notify user to retry
                                }
                                "The server was full. I'm clearing space now—please try sending again in 5 seconds!"
                            } else original
                        }
                    }
                    uiState = ChatUiState.Error("Could not send: $msg")
                }
            }
        }
    }

    fun deleteMessage(id: String) {
        viewModelScope.launch {
            val result = repository.deleteMessage(id)
            if (result is AppResult.Success) {
                load()
            } else if (result is AppResult.Failure) {
                // Optionally show error toast or update UI state
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val context = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as android.app.Application).applicationContext
                val db = com.example.myapplication.data.AppDatabase.get(context)
                ChatViewModel(
                    ChatRepositoryImpl(
                        NetworkModule.chatApi,
                        db.messageDao()
                    )
                )
            }
        }
    }
}
