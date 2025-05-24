package com.example.projet_session3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.projet_session3.screens.LoginScreen
import com.example.projet_session3.screens.MotDePasseOublieScreen
import com.example.projet_session3.screens.RegisterScreen
import com.example.projet_session3.ui.theme.MapRoadRecorderTheme







class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false) // enableEdgeToEdge()
        setContent {
            MapRoadRecorderTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {

                    // Écran de connexion
                    composable("login") {
                        LoginScreen(
                            onRegisterClick = { navController.navigate("register") },
                            onForgotPasswordClick = { navController.navigate("motdepasseoublie") }
                        )
                    }

                    // Écran d'inscription
                    composable("register") {
                        RegisterScreen(
                            onRegisterSuccess = { navController.navigate("login") },
                            onLoginClick = { navController.navigate("login") }
                        )
                    }

                    // Écran mot de passe oublié
                    composable("motdepasseoublie") {
                        MotDePasseOublieScreen(
                            onRetourConnexion = { navController.navigate("login") }
                        )
                    }
                }
            }
        }
    }
}



