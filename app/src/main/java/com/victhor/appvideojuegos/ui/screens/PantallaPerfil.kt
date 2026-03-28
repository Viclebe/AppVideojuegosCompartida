package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.PerfilViewModel
import com.victhor.appvideojuegos.navigation.Routes
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PantallaPerfil(
    viewModel: PerfilViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()

    // Llamamos a cargar perfil apenas se abre la pantalla
    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator()
                return@Column
            }
            
            if (!uiState.perfilReconocido) {
                Text("No hay ningún usuario activo.")
                Button(onClick = { navController.navigate(Routes.Login.route) { popUpTo(0) } }) {
                    Text("Ir al Login")
                }
                return@Column
            }

            // Avatar inicial
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color.Gray),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = uiState.email.firstOrNull()?.uppercase() ?: "?",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text("Correo: ${uiState.email}", style = MaterialTheme.typography.titleMedium)

            Spacer(modifier = Modifier.height(8.dp))

            Text("Nombre: Registrado", style = MaterialTheme.typography.bodyLarge)

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.cerrarSesion()
                    navController.navigate(Routes.Login.route) {
                        popUpTo(0) // Borra todo el historial de navegación
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Cerrar sesión", color = MaterialTheme.colorScheme.onError)
            }
        }
    }
}