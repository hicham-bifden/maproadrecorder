package com.example.projet_session3.screens.Map

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import androidx.compose.ui.platform.LocalContext
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onLogout: () -> Unit,
    isDarkMode: Boolean,
    onThemeChanged: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val viewModel: TripViewModel = viewModel()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Map,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = "Map Track",
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Déconnexion")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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
                SettingsScreen(
                    context = context,
                    isDarkMode = isDarkMode,
                    onThemeChanged = onThemeChanged
                )
            }
            composable("tripDetail/{tripId}") { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId")
                val trip = viewModel.trips.value.find { it.id == tripId }
                if (trip != null) {
                    TripDetailScreen(
                        navController = navController,
                        trip = trip,
                        onSave = { updatedTrip ->
                            viewModel.updateTrip(updatedTrip)
                        },
                        onDelete = { tripId ->
                            viewModel.deleteTrip(tripId)
                        }
                    )
                }
            }
        }
    }
}
