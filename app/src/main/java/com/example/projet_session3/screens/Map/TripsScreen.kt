package com.example.projet_session3.screens.Map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projet_session3.R
import com.example.projet_session3.ViewModel.TripViewModel
import androidx.navigation.NavController
import com.example.projet_session3.ViewModel.Trip

@Composable
fun TripsScreen(
    navController: NavController,
    viewModel: TripViewModel = viewModel()
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    var tripToDelete by remember { mutableStateOf<Trip?>(null) }
    val trips by viewModel.trips.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val isAscending by viewModel.isAscending.collectAsState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.my_trips),
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Barre de recherche
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            placeholder = { Text("Rechercher...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Rechercher") },
            singleLine = true
        )

        // Bouton de tri
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = { viewModel.toggleSortOrder() }) {
                Icon(
                    if (isAscending) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                    contentDescription = "Trier",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        if (trips.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.no_trips),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray
                )
            }
        } else {
            LazyColumn {
                items(trips) { trip ->
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
                            Text(
                                text = trip.description,
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "📅 ${trip.date}",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.DarkGray
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                horizontalArrangement = Arrangement.End,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                IconButton(onClick = {
                                    navController.navigate("tripDetail/${trip.id}")
                                }) {
                                    Icon(Icons.Default.Info, contentDescription = "Détails", tint = Color(0xFF2196F3))
                                }

                                IconButton(onClick = {
                                    navController.navigate("tripDetail/${trip.id}")
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
        }
    }

    if (showDeleteDialog && tripToDelete != null) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Supprimer le voyage ?") },
            text = { Text("Es-tu sûr de vouloir supprimer \"${tripToDelete?.title}\" ?") },
            confirmButton = {
                TextButton(onClick = {
                    tripToDelete?.id?.let { viewModel.deleteTrip(it) }
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
