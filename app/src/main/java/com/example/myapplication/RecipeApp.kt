package com.example.myapplication

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun RecipeApp() {
    val navController = rememberNavController()
    // ONE ViewModel created here and shared by BOTH screens
    val viewModel: DishViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "dish_list"
    ) {
        // ----- SCREEN 1 -----
        composable(route = "dish_list") {
            DishListScreen(
                viewModel = viewModel,
                onDishClick = { dishId ->
                    // TODO 6: navigate to the detail screen for this dish.
                    navController.navigate("dish_detail/$dishId")
                }
            )
        }
        // ----- SCREEN 2 -----
        composable(
            route = "dish_detail/{dishId}",
            arguments = listOf(navArgument("dishId") { type = NavType.IntType })
        ) { backStackEntry ->
            val dishId = backStackEntry.arguments?.getInt("dishId") ?: 0
            DishDetailScreen(
                dishId = dishId,
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
