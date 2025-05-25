package com.example.projet_session3.screens.Map



import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SettingsScreen() {
    var isDarkTheme by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Thème sombre", style = MaterialTheme.typography.titleMedium)
        Switch(
            checked = isDarkTheme,
            onCheckedChange = { isDarkTheme = it }
        )
    }
}
