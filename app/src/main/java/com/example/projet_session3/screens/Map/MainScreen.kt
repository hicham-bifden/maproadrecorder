package com.example.projet_session3.screens.Map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projet_session3.ViewModel.TripViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.projet_session3.screens.Map.TripDetailScreen
import androidx.compose.ui.res.stringResource
import com.example.projet_session3.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val viewModel: TripViewModel = viewModel()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MapRoadRecorder") },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Map, contentDescription = stringResource(R.string.nav_map)) },
                    label = { Text(stringResource(R.string.nav_map)) },
                    selected = currentRoute == "map",
                    onClick = {
                        if (currentRoute != "map") {
                            navController.navigate("map") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = stringResource(R.string.nav_trips)) },
                    label = { Text(stringResource(R.string.nav_trips)) },
                    selected = currentRoute == "trips",
                    onClick = {
                        if (currentRoute != "trips") {
                            navController.navigate("trips") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = stringResource(R.string.nav_profile)) },
                    label = { Text(stringResource(R.string.nav_profile)) },
                    selected = currentRoute == "settings",
                    onClick = {
                        if (currentRoute != "settings") {
                            navController.navigate("settings") {
                                popUpTo(navController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "map",
            modifier = Modifier.padding(paddingValues)
        ) {
            composable("map") {
                MapScreen()
            }
            composable("trips") {
                TripsScreen(navController = navController, viewModel = viewModel)
            }
            composable("settings") {
                // TODO: Implémenter l'écran des paramètres
                Box(modifier = Modifier.fillMaxSize()) {
                    Text("Paramètres")
                }
            }
            composable("tripDetail/{tripId}") { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId")
                val selectedTrip = viewModel.trips.value.find { it.id == tripId }

                selectedTrip?.let {
                    TripDetailScreen(
                        navController = navController,
                        trip = it,
                        onSave = { updatedTrip ->
                            viewModel.updateTrip(updatedTrip)
                            navController.popBackStack()
                        }
                    )
                } ?: Text("Voyage introuvable")
            }
        }
    }
}
