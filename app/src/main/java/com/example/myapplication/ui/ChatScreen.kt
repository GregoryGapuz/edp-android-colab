package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.domain.Message

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory),
    themeVm: ThemeViewModel = viewModel(factory = ThemeViewModel.Factory)
) {
    val isDark by themeVm.isDarkTheme.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LiceoChat") },
                actions = {
                    IconButton(onClick = { themeVm.onThemeChanged(!isDark) }) {
                        Icon(
                            imageVector = if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme"
                        )
                    }
                    IconButton(onClick = { viewModel.load() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding).fillMaxSize()) {
            Box(Modifier.weight(1f).fillMaxWidth()) {
                when (val state = viewModel.uiState) {
                    ChatUiState.Loading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                    ChatUiState.Empty -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                        Text("No messages yet. Say hello!")
                    }
                    is ChatUiState.Ready -> LazyColumn(Modifier.fillMaxSize()) {
                        items(state.messages, key = { it.id }) { message ->
                            MessageRow(message)
                        }
                    }
                    is ChatUiState.Error -> Column(
                        Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(state.message)
                        Button(onClick = { viewModel.load() }) {
                            Text("Retry")
                        }
                    }
                }
            }
            MessageInput(
                name = viewModel.myName,
                draft = viewModel.draft,
                onNameChange = viewModel::onNameChange,
                onDraftChange = viewModel::onDraftChange,
                onSend = viewModel::send
            )
        }
    }
}

@Composable
fun MessageRow(message: Message) {
    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(message.sender, style = MaterialTheme.typography.labelLarge)
        Text(message.text, style = MaterialTheme.typography.bodyMedium)
    }
    HorizontalDivider()
}

@Composable
fun MessageInput(
    name: String,
    draft: String,
    onNameChange: (String) -> Unit,
    onDraftChange: (String) -> Unit,
    onSend: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(12.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Your full name") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = draft,
                onValueChange = onDraftChange,
                label = { Text("Message") },
                modifier = Modifier.weight(1f)
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = onSend) { Text("Send") }
        }
    }
}
