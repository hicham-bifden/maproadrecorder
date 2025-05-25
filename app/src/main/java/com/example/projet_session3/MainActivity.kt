package com.example.projet_session3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projet_session3.screens.Auth.LoginScreen
import com.example.projet_session3.screens.Auth.MotDePasseOublieScreen
import com.example.projet_session3.screens.Auth.RegisterScreen
import com.example.projet_session3.screens.Map.MainScreen
import com.example.projet_session3.ui.theme.MapRoadRecorderTheme







class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false) // enableEdgeToEdge()
        setContent {

            MapRoadRecorderTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "main") {

                    // Écran de connexion
                    composable("login") {
                        LoginScreen(
                            onRegisterClick = { navController.navigate("register") },
                            onForgotPasswordClick = { navController.navigate("motdepasseoublie")

                            },
                            onLoginSuccess = { navController.navigate("main") }

                        )
                    }

                    // Écran d'inscription
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { navController.navigate("login") },
                            onLoginClick = { navController.navigate("login") }
                        )
                    }

                    composable("main") {
                        MainScreen(navController)
                    }

                    // Écran mot de passe oublié
                    composable("motdepasseoublie") {
                        MotDePasseOublieScreen(
                            onEnvoyerLienReset = { email ->
                                // Ici tu peux appeler Firebase ou afficher un message
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



