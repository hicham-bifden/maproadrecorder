package com.example.projet_session3.screens.Map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projet_session3.ViewModel.TripViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val bottomNavController = rememberNavController()
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
                val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Map, contentDescription = "Carte") },
                    label = { Text("Carte") },
                    selected = currentRoute == "map",
                    onClick = {
                        if (currentRoute != "map") {
                            bottomNavController.navigate("map") {
                                popUpTo(bottomNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.List, contentDescription = "Voyages") },
                    label = { Text("Voyages") },
                    selected = currentRoute == "trips",
                    onClick = {
                        if (currentRoute != "trips") {
                            bottomNavController.navigate("trips") {
                                popUpTo(bottomNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )

                NavigationBarItem(
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Paramètres") },
                    label = { Text("Paramètres") },
                    selected = currentRoute == "settings",
                    onClick = {
                        if (currentRoute != "settings") {
                            bottomNavController.navigate("settings") {
                                popUpTo(bottomNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = bottomNavController,
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
        }
    }
}
