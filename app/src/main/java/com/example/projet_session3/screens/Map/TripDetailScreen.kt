package com.example.projet_session3.screens.Map

import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.projet_session3.ViewModel.Trip
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.compose.ui.platform.LocalContext
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch
import androidx.compose.ui.res.stringResource
import com.example.projet_session3.R
import kotlin.math.*

private const val TAG = "TripDetailScreen"

////

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripDetailScreen(
    navController: NavController,
    trip: Trip,
    onSave: (Trip) -> Unit,
    onDelete: (String) -> Unit
) {
    var isEditing by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf(trip.title) }
    var description by remember { mutableStateOf(trip.description) }
    val context = LocalContext.current
    
    val startPosition = trip.positions.firstOrNull()
    val endPosition = trip.positions.lastOrNull()
    
    val center = if (startPosition != null && endPosition != null) {
        LatLng(
            (startPosition.latitude + endPosition.latitude) / 2,
            (startPosition.longitude + endPosition.longitude) / 2
        )
    } else {
        LatLng(45.551164, -73.639164)
    }

    // Calcul des points de l'itinéraire
    val routePoints = remember(startPosition, endPosition) {
        if (startPosition != null && endPosition != null) {
            val points = mutableListOf<LatLng>()
            
            // Point de départ
            points.add(LatLng(startPosition.latitude, startPosition.longitude))
            
            // Calcul de la distance
            val latDiff = endPosition.latitude - startPosition.latitude
            val lngDiff = endPosition.longitude - startPosition.longitude
            
            // Création de points intermédiaires
            for (i in 1..10) {
                val fraction = i / 11.0
                val lat = startPosition.latitude + (latDiff * fraction)
                val lng = startPosition.longitude + (lngDiff * fraction)
                
                // Ajout d'une légère déviation
                val deviation = 0.0002 * Math.sin(i * Math.PI / 3)
                points.add(LatLng(lat + deviation, lng + deviation))
            }
            
            // Point d'arrivée
            points.add(LatLng(endPosition.latitude, endPosition.longitude))
            points
        } else {
            emptyList()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditing) stringResource(R.string.edit_trip) else stringResource(R.string.trip_details)) },
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
                        IconButton(onClick = { showDeleteDialog = true }) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color.Red)
                        }
                    }
                }
            )
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
                    label = { Text(stringResource(R.string.title)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text(stringResource(R.string.description)) },
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
                    Text(stringResource(R.string.save))
                }
            } else {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = description,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "📅 ${trip.date}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }

                if (startPosition != null && endPosition != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        elevation = CardDefaults.cardElevation(4.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(center, 12f)
                            }
                        ) {
                            Marker(
                                state = MarkerState(
                                    position = LatLng(startPosition.latitude, startPosition.longitude)
                                ),
                                title = "Départ",
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Point de départ\nLat: ${startPosition.latitude}\nLng: ${startPosition.longitude}\nDate: ${trip.date}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    true
                                }
                            )

                            Marker(
                                state = MarkerState(
                                    position = LatLng(endPosition.latitude, endPosition.longitude)
                                ),
                                title = "Arrivée",
                                onClick = {
                                    Toast.makeText(
                                        context,
                                        "Point d'arrivée\nLat: ${endPosition.latitude}\nLng: ${endPosition.longitude}\nDate: ${trip.date}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    true
                                }
                            )

                            Polyline(
                                points = routePoints,
                                color = MaterialTheme.colorScheme.primary,
                                width = 8f
                            )
                        }
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer le voyage ?") },
            text = { Text("Es-tu sûr de vouloir supprimer \"${trip.title}\" ?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDelete(trip.id)
                        showDeleteDialog = false
                        navController.popBackStack()
                    }
                ) {
                    Text("Oui", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Annuler")
                }
            }
        )
    }
}