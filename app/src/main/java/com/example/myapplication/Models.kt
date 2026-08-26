package com.example.myapplication

// One line of a recipe, e.g. "Marinate the pork for 30 minutes".
data class Recipe(
    val id: Int,
    val text: String
)

// One dish. It OWNS its list of recipe steps.
data class Dish(
    val id: Int,
    val name: String,
    val recipes: List<Recipe> = emptyList()
)
