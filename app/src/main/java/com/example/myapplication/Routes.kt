package com.example.myapplication

import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
data class Greeting(val userName: String)
