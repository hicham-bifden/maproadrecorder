package com.example.projet_session3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projet_session3.helper.PrefHelper
import com.example.projet_session3.model.Trip
import com.example.projet_session3.screens.Auth.LoginScreen
import com.example.projet_session3.screens.Auth.MotDePasseOublieScreen
import com.example.projet_session3.screens.Auth.RegisterScreen
import com.example.projet_session3.screens.Map.MainScreen
import com.example.projet_session3.screens.Map.TripDetailScreen
import com.example.projet_session3.screens.Map.TripsScreen
import com.example.projet_session3.ui.theme.MapRoadRecorderTheme







class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val prefHelper = PrefHelper(this)
            prefHelper.isDarkModeEnabled()

            MapRoadRecorderTheme {
                val navController = rememberNavController()

                // Exemple de liste fictive de trips (à remplacer par ViewModel plus tard)
                val tripsList = remember {
                    mutableStateOf(
                        listOf(
                            Trip(id = "1", title = "Voyage à Paris", description = "Un beau voyage", date = "2025-06-01"),
                            Trip(id = "2", title = "Safari au Kenya", description = "Aventure dans la savane", date = "2025-07-15")
                        )
                    )
                }

                NavHost(navController = navController, startDestination = "main") {

                    composable("login") {
                        LoginScreen(
                            onRegisterClick = { navController.navigate("register") },
                            onForgotPasswordClick = { navController.navigate("motdepasseoublie") },
                            onLoginSuccess = { navController.navigate("main") }
                        )
                    }

                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { navController.navigate("login") },
                            onLoginClick = { navController.navigate("login") }
                        )
                    }

                    composable("main") {
                        // Affichage de la liste des voyages
                        TripsScreen(navController, tripsList.value)
                    }

                    composable("tripDetail/{tripId}") { backStackEntry ->
                        val tripId = backStackEntry.arguments?.getString("tripId")
                        val selectedTrip = tripsList.value.find { it.id == tripId }

                        selectedTrip?.let {
                            TripDetailScreen(
                                navController = navController,
                                trip = it,
                                onSave = {
                                    navController.popBackStack()
                                }
                            )
                        } ?: Text("Voyage introuvable")
                    }

                    composable("motdepasseoublie") {
                        MotDePasseOublieScreen(
                            onEnvoyerLienReset = { email ->
                                println("Lien de réinitialisation envoyé à $email")
                            },
                            onRetourConnexion = { navController.navigate("login") }
                        )
                    }
                }
            }
        }
    }
}
