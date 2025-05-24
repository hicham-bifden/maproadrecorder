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
import com.example.projet_session3.screens.LoginScreen
import com.example.projet_session3.screens.MotDePasseOublieScreen
import com.example.projet_session3.screens.RegisterScreen
import com.example.projet_session3.ui.theme.MapRoadRecorderTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapRoadRecorderTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    MotDePasseOublieScreen(
                        onEnvoyerLienReset = { email ->
                            // Ici, tu peux gérer l'envoi du lien de reset
                            println("Demande de reset pour : $email")
                        },
                        onRetourConnexion = {
                            // Ici, tu peux gérer la navigation vers l'écran de connexion
                            println("Retour vers l'écran de connexion")
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MapRoadRecorderTheme {
        Greeting("Android")
    }
}