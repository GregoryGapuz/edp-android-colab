package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.ui.ChatScreen
import com.example.myapplication.ui.ThemeViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val themeVm: ThemeViewModel = viewModel(factory = ThemeViewModel.Factory)
            val isDark by themeVm.isDarkTheme.collectAsState()

            MyApplicationTheme(darkTheme = isDark) {
                ChatScreen(themeVm = themeVm)
            }
        }
    }
}
