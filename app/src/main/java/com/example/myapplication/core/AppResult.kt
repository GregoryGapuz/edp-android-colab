package com.example.myapplication.core

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    sealed interface Failure : AppResult<Nothing> {
        data object NoInternet : Failure
        data object Timeout : Failure
        data class Unknown(val message: String?) : Failure
    }
}
