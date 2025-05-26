package com.example.projet_session3.screens.Map

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.projet_session3.helper.AuthPrefHelper
import androidx.appcompat.app.AppCompatDelegate

@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
    onThemeChanged: (Boolean) -> Unit
) {
    val authPrefHelper = remember { AuthPrefHelper(context) }
    var isDarkMode by remember { mutableStateOf(authPrefHelper.isDarkMode()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Paramètres",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

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
