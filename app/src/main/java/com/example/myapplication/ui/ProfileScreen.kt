package com.example.myapplication.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ProfileScreen(postsVm: PostsViewModel, themeVm: ThemeViewModel) {

    val posts by postsVm.posts.collectAsStateWithLifecycle()

    val darkTheme by themeVm.isDarkTheme.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(Icons.Default.AccountCircle, null, Modifier.size(96.dp))
        Spacer(Modifier.height(8.dp))
        Text("Gregory Gapz", style = MaterialTheme.typography.headlineSmall)
        Text("@GregGapz", style = MaterialTheme.typography.bodyMedium)
        Text("BSIT-3 · Liceo de Cagayan University",
             style = MaterialTheme.typography.bodySmall)
        Spacer(Modifier.height(16.dp))
        Text("${posts.size} posts", style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text("Dark theme", modifier = Modifier.weight(1f))
            Switch(
                checked = darkTheme,
                onCheckedChange = { themeVm.onThemeChanged(it) },
            )
        }
    }
}
