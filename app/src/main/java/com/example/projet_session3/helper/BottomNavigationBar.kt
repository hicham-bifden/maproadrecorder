package com.example.projet_session3.helper

// BottomNavigationBar.kt

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController) {
    val items = listOf(
        NavigationItem.Map,
        NavigationItem.Trips,
        NavigationItem.Settings
    )

    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Évite les multiples copies de la même destination
                        launchSingleTop = true
                        // Restaure l'état quand on reclique sur le même item
                        restoreState = true
                    }
                },
                icon = { Icon(item.icon, contentDescription = item.title) },
                label = { Text(item.title) }
            )
        }
    }
}

sealed class NavigationItem(val route: String, val title: String, val icon: ImageVector) {
    object Map : NavigationItem("map", "Carte", Icons.Default.Map)
    object Trips : NavigationItem("trips", "Voyages", Icons.Default.List)
    object Settings : NavigationItem("settings", "Paramètres", Icons.Default.Settings)
}


