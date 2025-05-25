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
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.projet_session3.model.Trip
import androidx.compose.ui.graphics.Color


////


@Composable
fun TripsScreen(tripsList: List<Trip>) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
    ) {
        Text(
            text = "Mes Voyages",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        tripsList.forEach { trip ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                elevation = CardDefaults.cardElevation(6.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(text = trip.title, style = MaterialTheme.typography.titleLarge)
                    Text(text = trip.description, style = MaterialTheme.typography.bodyMedium, color = Color.Gray)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "📅 ${trip.date}", style = MaterialTheme.typography.labelSmall, color = Color.DarkGray)

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = {
                            // TODO: Naviguer vers les détails du voyage
                        }) {
                            Icon(Icons.Default.Info, contentDescription = "Détails", tint = Color(0xFF2196F3))
                        }

                        IconButton(onClick = {
                            // TODO: Naviguer vers l'écran de modification
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Modifier", tint = Color(0xFFFF9800))
                        }

                        IconButton(onClick = {
                            tripToDelete = trip
                            showDeleteDialog = true
                        }) {
                            Icon(Icons.Default.Delete, contentDescription = "Supprimer", tint = Color(0xFFF44336))
                        }
                    }
                }
            }
        }
    }

    // Boîte de confirmation de suppression
    if (showDeleteDialog && tripToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer le voyage ?") },
            text = { Text("Es-tu sûr de vouloir supprimer \"${tripToDelete?.title}\" ?") },
            confirmButton = {
                TextButton(onClick = {
                    // TODO: Supprimer le voyage de la liste réelle
                    showDeleteDialog = false
                }) {
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
