package com.example.projet_session3.screens.Map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.projet_session3.ViewModel.Trip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import com.example.projet_session3.helper.BottomNavigationBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    navController: NavController,
    trip: Trip,
    onSave: (Trip) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(trip.title) }
    var description by remember { mutableStateOf(trip.description) }

    // Récupérer les points de départ et d'arrivée
    val startPosition = trip.positions.firstOrNull()
    val endPosition = trip.positions.lastOrNull()

    // Calculer le centre de la carte
    val center = if (startPosition != null && endPosition != null) {
        LatLng(
            (startPosition.latitude + endPosition.latitude) / 2,
            (startPosition.longitude + endPosition.longitude) / 2
        )
    } else {
        LatLng(45.551164, -73.639164) // Position par défaut (Montréal)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) "Modifier le voyage" else "Détails du voyage") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                    }
                },
                actions = {
                    if (!isEditing) {
                        IconButton(onClick = { isEditing = true }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier")
                        }
                    }
                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            if (isEditing) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Titre") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        onSave(trip.copy(title = title, description = description))
                        isEditing = false
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Enregistrer")
                }
            } else {
                Text("📌 $title", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(description, style = MaterialTheme.typography.bodyLarge)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    "📅 Date : ${trip.date}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))

                // Carte avec les points
                if (startPosition != null && endPosition != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(center, 12f)
                            }
                        ) {
                            // Marqueur de départ
                            Marker(
                                state = MarkerState(
                                    position = LatLng(startPosition.latitude, startPosition.longitude)
                                ),
                                title = "Départ"
                            )

                            // Marqueur d'arrivée
                            Marker(
                                state = MarkerState(
                                    position = LatLng(endPosition.latitude, endPosition.longitude)
                                ),
                                title = "Arrivée"
                            )

                            // Ligne droite entre les points
                            Polyline(
                                points = listOf(
                                    LatLng(startPosition.latitude, startPosition.longitude),
                                    LatLng(endPosition.latitude, endPosition.longitude)
                                ),
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}