package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.viewmodel.LoginViewModel

@Composable
fun PantallaLogin(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val state by viewModel.uiState.collectAsState() // Obtener estado desde LoginViewModel

    LaunchedEffect(state.loginExitoso) {
        if (state.loginExitoso) { // Observar si loginExitoso para navegar
            navController.navigate(Routes.Principal.route) {
                popUpTo(Routes.Login.route) { inclusive = true } // Evitar volver pantalla atrás
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // LOGO
        Image(
            painter = painterResource(id = R.drawable.logonombre),
            contentDescription = "Logo app",
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp), // Reducir un poco
            contentScale = ContentScale.Fit
        )

        // Texto
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Juega · Guarda · Recuerda",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Convierte cada partida en memoria",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // CAMPOS PARA INICIAR SESIÓN
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.cambiarEmail(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.cambiarPassword(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation() // Ocultar contraseña
            )

            // MOSTRAR ERROR SI HUBIERA
            if (state.error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // BOTONES
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // BOTÓN INICIAR SESIÓN
            Button(
                onClick = { viewModel.iniciarSesion() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Iniciar sesión")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // BOTÓN REGISTRARSE
            Button(
                onClick = { viewModel.registrarUsuario() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !state.isLoading
            ) {
                Text(
                    text = "Registrarse",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}