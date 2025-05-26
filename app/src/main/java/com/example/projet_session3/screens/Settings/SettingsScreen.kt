package com.example.projet_session3.screens.Settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.projet_session3.R
import com.example.projet_session3.helper.AuthPrefHelper

@Composable
fun SettingsScreen(
    authPrefHelper: AuthPrefHelper,
    onThemeChanged: (Boolean) -> Unit
) {
    var isDarkMode by remember { mutableStateOf(authPrefHelper.isDarkMode()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête
        Text(
            text = "Paramètres",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Switch pour le mode sombre
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mode sombre",
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isDarkMode,
                onCheckedChange = { newValue ->
                    isDarkMode = newValue
                    authPrefHelper.saveDarkMode(newValue)
                    onThemeChanged(newValue)
                }
            )
        }
    }
} 