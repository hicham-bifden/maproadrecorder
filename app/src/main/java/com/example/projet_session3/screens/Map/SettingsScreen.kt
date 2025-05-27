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
import androidx.compose.ui.res.stringResource
import com.example.projet_session3.R

@Composable
fun SettingsScreen(
    context: Context = LocalContext.current,
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val authPrefHelper = remember { AuthPrefHelper(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.settings),
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
                text = stringResource(R.string.dark_mode),
                style = MaterialTheme.typography.bodyLarge
            )
            Switch(
                checked = isDarkMode,
                onCheckedChange = { newValue ->
                    authPrefHelper.saveDarkMode(newValue)
                    onThemeChanged(newValue)
                }
            )
        }
    }
}
