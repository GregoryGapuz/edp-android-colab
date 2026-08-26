package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DishDetailScreen(
    dishId: Int,
    viewModel: DishViewModel,
    onBack: () -> Unit
) {
    val dishes by viewModel.dishes.collectAsStateWithLifecycle()
    val dish = dishes.find { it.id == dishId }

    if (dish == null) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            TextButton(onClick = onBack) { Text("< Back") }
            Text("Dish not found")
        }
        return
    }

    var newStep by remember { mutableStateOf("") }
    var stepBeingEdited by remember { mutableStateOf<Recipe?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        TextButton(onClick = onBack) { Text("< Back") }
        Text(dish.name, style = MaterialTheme.typography.headlineMedium)
        Text("Recipe steps", style = MaterialTheme.typography.bodyMedium)
        Spacer(Modifier.height(12.dp))

        // TODO 9 -- CREATE
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = newStep,
                onValueChange = { newStep = it },
                label = { Text("New step") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                viewModel.addRecipe(dishId, newStep)
                newStep = ""
            }) { Text("Add") }
        }

        Spacer(Modifier.height(16.dp))

        // TODO 10 -- READ + DELETE
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            itemsIndexed(items = dish.recipes, key = { _, r -> r.id }) { index, recipe ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("${index + 1}. ${recipe.text}", modifier = Modifier.weight(1f))
                    TextButton(onClick = { stepBeingEdited = recipe }) { Text("Edit") }
                    TextButton(onClick = {
                        viewModel.deleteRecipe(dishId, recipe.id)
                    }) { Text("Delete") }
                }
            }
        }
    }

    // TODO 11 -- UPDATE
    val editing = stepBeingEdited
    if (editing != null) {
        EditDialog(
            title = "Edit recipe step",
            initialText = editing.text,
            onConfirm = { newText ->
                viewModel.updateRecipe(dishId, editing.id, newText)
                stepBeingEdited = null
            },
            onDismiss = { stepBeingEdited = null }
        )
    }
}
