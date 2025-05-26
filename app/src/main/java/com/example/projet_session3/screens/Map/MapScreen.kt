package com.example.projet_session3.screens.Map

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.Button
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.*
import android.Manifest
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projet_session3.ViewModel.TripViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

// Position par défaut (Montréal)
private val defaultLocation = LatLng(45.551164, -73.639164)

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen() {
    val viewModel: TripViewModel = viewModel()
    val isRecording by viewModel.isRecording.collectAsState()
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)
    
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) {
        if (!locationPermissionState.status.isGranted) {
            Button(
                onClick = { locationPermissionState.launchPermissionRequest() },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Demander la permission de localisation")
            }
        } else {
            // Carte Google Maps
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(defaultLocation, 15f)
                }
            )

            // Bouton Start/Stop
            Button(
                onClick = {
                    if (isRecording) {
                        showDialog = true
                    } else {
                        viewModel.startRecording(defaultLocation)
                    }
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            ) {
                Text(if (isRecording) "Arrêter" else "Commencer")
            }
        }
    }

    // Dialog pour saisir le titre et la description
    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Enregistrer le voyage") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Titre") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = description,
                        onValueChange = { description = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (title.isNotBlank()) {
                            viewModel.stopRecording(defaultLocation, title, description)
                            showDialog = false
                            title = ""
                            description = ""
                        }
                    }
                ) {
                    Text("Enregistrer")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}
