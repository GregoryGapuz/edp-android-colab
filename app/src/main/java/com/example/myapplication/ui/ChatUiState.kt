package com.example.myapplication.ui

import com.example.myapplication.domain.Message

sealed interface ChatUiState {
    data object Loading : ChatUiState
    data object Empty : ChatUiState
    data class Ready(val messages: List<Message>) : ChatUiState
    data class Error(val message: String) : ChatUiState
}
