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
import com.example.projet_session3.helper.AuthPrefHelper
import com.example.projet_session3.helper.PrefHelper
import com.example.projet_session3.model.Trip
import com.example.projet_session3.screens.Auth.LoginScreen
import com.example.projet_session3.screens.Auth.MotDePasseOublieScreen
import com.example.projet_session3.screens.Auth.RegisterScreen
import com.example.projet_session3.screens.Map.MainScreen
import com.example.projet_session3.screens.Map.TripDetailScreen
import com.example.projet_session3.screens.Map.TripsScreen
import com.example.projet_session3.ui.theme.MapRoadRecorderTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val prefHelper = PrefHelper(this)
            val authPrefHelper = AuthPrefHelper(this)
            prefHelper.isDarkModeEnabled()

            MapRoadRecorderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    // Liste simulée de voyages (à remplacer par un ViewModel plus tard)
                    val tripsList = remember {
                        mutableStateOf(
                            listOf(
                                Trip(id = "1", title = "Voyage à Paris", description = "Un beau voyage", date = "2025-06-01"),
                                Trip(id = "2", title = "Safari au Kenya", description = "Aventure dans la savane", date = "2025-07-15")
                            )
                        )
                    }

                    NavHost(
                        navController = navController,
                        startDestination = if (authPrefHelper.isUserLoggedIn()) "main" else "login"
                    ) {
                        composable("login") {
                            LoginScreen(
                                onRegisterClick = { navController.navigate("register") },
                                onForgotPasswordClick = { navController.navigate("motDePasseOublie") },
                                onLoginSuccess = { 
                                    authPrefHelper.saveUserLoggedIn(true)
                                    navController.navigate("main") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("register") {
                            RegisterScreen(
                                onLoginClick = { navController.navigate("login") },
                                onRegisterSuccess = { navController.navigate("login") }
                            )
                        }
                        composable("motDePasseOublie") {
                            MotDePasseOublieScreen(
                                onBackToLogin = { navController.navigate("login") }
                            )
                        }
                        composable("main") {
                            MainScreen(
                                onLogout = {
                                    authPrefHelper.clearUserData()
                                    navController.navigate("login") {
                                        popUpTo("main") { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable("tripDetail/{tripId}") { backStackEntry ->
                            val tripId = backStackEntry.arguments?.getString("tripId")
                            val selectedTrip = tripsList.value.find { it.id == tripId }

                            selectedTrip?.let {
                                TripDetailScreen(
                                    navController = navController,
                                    trip = it,
                                    onSave = { updatedTrip ->
                                        // Mettre à jour la liste des voyages
                                        val updatedList = tripsList.value.map { trip ->
                                            if (trip.id == updatedTrip.id) updatedTrip else trip
                                        }
                                        tripsList.value = updatedList
                                        navController.popBackStack()
                                    }
                                )
                            } ?: Text("Voyage introuvable")
                        }
                    }
                }
            }
        }
    }
}
