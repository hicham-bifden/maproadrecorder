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
import com.example.projet_session3.helper.PrefHelper
import kotlin.math.log


@Composable
fun SettingsScreen(context: Context = LocalContext.current) {
    val prefHelper = remember { PrefHelper(context) }
    var isDarkMode by remember { mutableStateOf(prefHelper.isDarkModeEnabled()) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "Mode sombre", modifier = Modifier.weight(1f))
        Switch(
            checked = isDarkMode,
            onCheckedChange = {
                isDarkMode = it
                prefHelper.setDarkMode(it)
            }
        )
    }
}
