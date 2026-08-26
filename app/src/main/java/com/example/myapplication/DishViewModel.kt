package com.example.myapplication

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class DishViewModel : ViewModel() {
    // The ONE source of truth for the whole app.
    private val _dishes = MutableStateFlow(
        listOf(
            Dish(id = 1, name = "Chicken Adobo"),
            Dish(id = 2, name = "Sinigang na Baboy")
        )
    )
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    // Used to hand out a fresh, unique id every time we create something.
    private var nextId = 100

    // ---------------- DISH CRUD ----------------
    fun addDish(name: String) {
        if (name.isBlank()) return
        val newDish = Dish(id = nextId++, name = name.trim())
        _dishes.value = _dishes.value + newDish
    }

    fun getDish(dishId: Int): Dish? {
        return _dishes.value.find { it.id == dishId }
    }

    // TODO 1: Rename the dish whose id == dishId. Ignore a blank newName.
    fun updateDish(dishId: Int, newName: String) {
        if (newName.isBlank()) return
        _dishes.value = _dishes.value.map {
            if (it.id == dishId) it.copy(name = newName.trim()) else it
        }
    }

    // TODO 2: Remove the dish whose id == dishId.
    fun deleteDish(dishId: Int) {
        _dishes.value = _dishes.value.filter { it.id != dishId }
    }

    // ---------------- RECIPE CRUD ----------------
    // TODO 3: Add a new Recipe(id = nextId++, text = text.trim()) to the dish whose id == dishId.
    fun addRecipe(dishId: Int, text: String) {
        if (text.isBlank()) return
        val newRecipe = Recipe(id = nextId++, text = text.trim())
        _dishes.value = _dishes.value.map { dish ->
            if (dish.id == dishId) {
                dish.copy(recipes = dish.recipes + newRecipe)
            } else {
                dish
            }
        }
    }

    // TODO 4: Change the text of the recipe whose id == recipeId, inside the dish whose id == dishId.
    fun updateRecipe(dishId: Int, recipeId: Int, newText: String) {
        if (newText.isBlank()) return
        _dishes.value = _dishes.value.map { dish ->
            if (dish.id == dishId) {
                dish.copy(recipes = dish.recipes.map { recipe ->
                    if (recipe.id == recipeId) recipe.copy(text = newText.trim()) else recipe
                })
            } else {
                dish
            }
        }
    }

    // TODO 5: Remove the recipe whose id == recipeId from the dish whose id == dishId.
    fun deleteRecipe(dishId: Int, recipeId: Int) {
        _dishes.value = _dishes.value.map { dish ->
            if (dish.id == dishId) {
                dish.copy(recipes = dish.recipes.filter { it.id != recipeId })
            } else {
                dish
            }
        }
    }
}
