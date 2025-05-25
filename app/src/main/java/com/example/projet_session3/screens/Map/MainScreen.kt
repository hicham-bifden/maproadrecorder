package com.example.projet_session3.screens.Map

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.projet_session3.model.Trip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    navController: NavController,
    tripsList: List<Trip>
) {
    val bottomNavController = rememberNavController()
    val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("MapRoadRecorder") },
                actions = {
                    IconButton(onClick = {
                        // Déconnexion → retourne à l'écran de login
                        navController.navigate("login") {
                            popUpTo("main") { inclusive = true }
                        }
                    }) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = currentRoute == "map",
                    onClick = {
                        if (currentRoute != "map") {
                            bottomNavController.navigate("map") {
                                popUpTo(bottomNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Map, contentDescription = "Carte") },
                    label = { Text("Carte") }
                )
                NavigationBarItem(
                    selected = currentRoute == "trips",
                    onClick = {
                        if (currentRoute != "trips") {
                            bottomNavController.navigate("trips") {
                                popUpTo(bottomNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.List, contentDescription = "Voyages") },
                    label = { Text("Voyages") }
                )
                NavigationBarItem(
                    selected = currentRoute == "settings",
                    onClick = {
                        if (currentRoute != "settings") {
                            bottomNavController.navigate("settings") {
                                popUpTo(bottomNavController.graph.startDestinationId)
                                launchSingleTop = true
                            }
                        }
                    },
                    icon = { Icon(Icons.Default.Settings, contentDescription = "Paramètres") },
                    label = { Text("Paramètres") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = "map",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("map") {
                MapScreen()
            }
            composable("trips") {
                TripsScreen(
                    navController = navController,
                    tripsList = tripsList
                )
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}
