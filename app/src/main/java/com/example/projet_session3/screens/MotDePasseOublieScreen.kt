package com.example.projet_session3.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
@Composable
fun MotDePasseOublieScreen(
    onEnvoyerLienReset: (String) -> Unit,
    onRetourConnexion: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var messageEnvoye by remember { mutableStateOf(false) }
    var messageErreur by remember { mutableStateOf<String?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Réinitialiser le mot de passe",
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            isError = messageErreur != null,
            modifier = Modifier.fillMaxWidth()
        )
        if (messageErreur != null) {
            Text(
                text = messageErreur!!,

                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (email.isBlank()) {
                    messageErreur = "Veuillez entrer un email valide"
                } else {
                    messageErreur = null
                    onEnvoyerLienReset(email)
                    messageEnvoye = true
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Envoyer le lien de réinitialisation")
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (messageEnvoye) {
            Text(
                text = "Un lien de réinitialisation a été envoyé à votre email.",
                //color = MaterialTheme.colors.primary,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Vous vous êtes souvenu ? ")
            TextButton(onClick = onRetourConnexion) {
                Text("Se connecter")
            }
        }
    }
}
