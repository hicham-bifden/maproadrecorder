package com.example.projet_session3.screens.Map


import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.*


import android.Manifest


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen() {
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    if (!locationPermissionState.status.isGranted) {
        Button(onClick = { locationPermissionState.launchPermissionRequest() }) {
            Text("Demander la permission de localisation")
        }
    } else {
        Text("Permission accordée, afficher la carte ici")
    }
}
