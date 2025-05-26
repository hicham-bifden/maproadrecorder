package com.example.projet_session3.screens.Auth

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.projet_session3.model.AuthState
import com.example.projet_session3.model.AuthViewModel

@Composable

fun MotDePasseOublieScreen(
    onBackToLogin: () -> Unit,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    val authState by viewModel.authState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Réinitialisation du mot de passe", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth()
        )

        if (authState is AuthState.Error) {
            Text(
                text = (authState as AuthState.Error).message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (authState is AuthState.Success) {
            Text(
                text = "Un email de réinitialisation a été envoyé à votre adresse email.",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { viewModel.resetPassword(email) },
            modifier = Modifier.fillMaxWidth(),
            enabled = authState !is AuthState.Loading
        ) {
            if (authState is AuthState.Loading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary)
            } else {
                Text("Envoyer le lien de réinitialisation")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onBackToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Retour à la connexion")
        }
    }
}
