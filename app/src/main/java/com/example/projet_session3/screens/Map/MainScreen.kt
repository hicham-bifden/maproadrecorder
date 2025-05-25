package com.example.projet_session3.screens.Map

import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

import androidx.compose.runtime.remember
 import androidx.compose.ui.Modifier
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
 import com.example.projet_session3.model.Trip




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    val bottomNavController = rememberNavController()

    // Exemple de liste fictive de voyages
    val fakeTrips = remember {
        listOf(
            Trip("1", "Voyage à Marrakech", "Balade dans le désert", "2025-05-23"),
            Trip("2", "Week-end à Québec", "Visite du Vieux-Québec", "2025-05-01")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bonjour, utilisateur") },
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
                val currentRoute = bottomNavController.currentBackStackEntryAsState().value?.destination?.route

                NavigationBarItem(
                    selected = currentRoute == "map",
                    onClick = { bottomNavController.navigate("map") },
                    icon = { Icon(Icons.Default.Map, contentDescription = "Carte") },
                    label = { Text("Carte") }
                )
                NavigationBarItem(
                    selected = currentRoute == "trips",
                    onClick = { bottomNavController.navigate("trips") },
                    icon = { Icon(Icons.Default.List, contentDescription = "Voyages") },
                    label = { Text("Voyages") }
                )
                NavigationBarItem(
                    selected = currentRoute == "settings",
                    onClick = { bottomNavController.navigate("settings") },
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
                TripsScreen(tripsList = fakeTrips, navController = navController)
            }
            composable("settings") {
                SettingsScreen()
            }
        }
    }
}
