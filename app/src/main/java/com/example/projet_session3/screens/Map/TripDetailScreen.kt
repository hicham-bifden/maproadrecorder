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

private const val TAG = "TripDetailScreen"

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
    var showRoute by remember { mutableStateOf(false) }
    var routePoints by remember { mutableStateOf<List<LatLng>>(emptyList()) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

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
                        Box(modifier = Modifier.fillMaxSize()) {
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
                                    title = "Départ"
                                )

                                Marker(
                                    state = MarkerState(
                                        position = LatLng(endPosition.latitude, endPosition.longitude)
                                    ),
                                    title = "Arrivée"
                                )

                                if (showRoute && routePoints.isNotEmpty()) {
                                    Polyline(
                                        points = routePoints,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Polyline(
                                        points = listOf(
                                            LatLng(startPosition.latitude, startPosition.longitude),
                                            LatLng(endPosition.latitude, endPosition.longitude)
                                        ),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }

                            // Bouton flottant avec icône de voiture
                            FloatingActionButton(
                                onClick = {
                                    Log.d(TAG, "Bouton cliqué - showRoute: $showRoute")
                                    Toast.makeText(context, "Chargement de l'itinéraire...", Toast.LENGTH_SHORT).show()
                                    
                                    // Vérifier les positions
                                    Log.d(TAG, "Position de départ: lat=${startPosition.latitude}, lng=${startPosition.longitude}")
                                    Log.d(TAG, "Position d'arrivée: lat=${endPosition.latitude}, lng=${endPosition.longitude}")
                                    
                                    showRoute = !showRoute
                                    if (showRoute) {
                                        scope.launch {
                                            try {
                                                // Créer une liste de points intermédiaires pour simuler un itinéraire
                                                val points = mutableListOf<LatLng>()
                                                
                                                // Point de départ
                                                points.add(LatLng(startPosition.latitude, startPosition.longitude))
                                                
                                                // Points intermédiaires
                                                val latDiff = endPosition.latitude - startPosition.latitude
                                                val lngDiff = endPosition.longitude - startPosition.longitude
                                                
                                                // Ajouter 5 points intermédiaires
                                                for (i in 1..5) {
                                                    val fraction = i / 6.0
                                                    val lat = startPosition.latitude + (latDiff * fraction)
                                                    val lng = startPosition.longitude + (lngDiff * fraction)
                                                    points.add(LatLng(lat, lng))
                                                    Log.d(TAG, "Point intermédiaire $i: lat=$lat, lng=$lng")
                                                }
                                                
                                                // Point d'arrivée
                                                points.add(LatLng(endPosition.latitude, endPosition.longitude))
                                                
                                                routePoints = points
                                                Log.d(TAG, "Itinéraire simulé créé avec ${points.size} points")
                                                Toast.makeText(context, "Itinéraire chargé !", Toast.LENGTH_SHORT).show()
                                                
                                            } catch (e: Exception) {
                                                Log.e(TAG, "Erreur détaillée", e)
                                                Log.e(TAG, "Message d'erreur: ${e.message}")
                                                Log.e(TAG, "Stack trace: ${e.stackTraceToString()}")
                                                Toast.makeText(context, "Erreur: ${e.message}", Toast.LENGTH_LONG).show()
                                                showRoute = false
                                            }
                                        }
                                    } else {
                                        routePoints = emptyList()
                                        Log.d(TAG, "Retour à la ligne droite")
                                        Toast.makeText(context, "Retour à la ligne droite", Toast.LENGTH_SHORT).show()
                                    }
                                },
                                modifier = Modifier
                                    .padding(16.dp)
                                    .align(Alignment.BottomEnd)
                            ) {
                                Icon(
                                    imageVector = if (showRoute) Icons.Default.DirectionsWalk else Icons.Default.DirectionsCar,
                                    contentDescription = if (showRoute) "Afficher ligne droite" else "Afficher itinéraire routier"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}