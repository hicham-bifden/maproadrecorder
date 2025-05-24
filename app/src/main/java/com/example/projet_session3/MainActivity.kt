package com.example.projet_session3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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



