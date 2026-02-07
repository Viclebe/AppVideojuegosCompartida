package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold

@Composable
fun PantallaInsertar(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    AppScaffold {
        ContenidoPantallaInsertar(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun ContenidoPantallaInsertar(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    var titulo by remember { mutableStateOf("") }
    var genero by remember { mutableStateOf("") }
    var plataforma by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf("") }
    var horasJugadas by remember { mutableStateOf("") }
    var valoracion by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {

        Text(
            text = "Añadir videojuego",
            style = MaterialTheme.typography.headlineSmall
        )

        OutlinedTextField(
            value = titulo,
            onValueChange = { titulo = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = genero,
            onValueChange = { genero = it },
            label = { Text("Género") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = plataforma,
            onValueChange = { plataforma = it },
            label = { Text("Plataforma") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = estado,
            onValueChange = { estado = it },
            label = { Text("Estado (Jugando / Pendiente / Finalizado)") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = horasJugadas,
            onValueChange = { horasJugadas = it },
            label = { Text("Horas jugadas") },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = valoracion,
            onValueChange = { valoracion = it },
            label = { Text("Valoración (0-5)") },
            modifier = Modifier.fillMaxWidth()
        )
    }
    Spacer(modifier = Modifier.height(20.dp))

    Button(
        onClick = {
            if (titulo.isNotBlank()) {
                val videojuego = Videojuego(
                    titulo = titulo,
                    genero = genero,
                    plataforma = plataforma,
                    estado = estado,
                    horasJugadas = horasJugadas.toIntOrNull() ?: 0,
                    valoracion = valoracion.toDoubleOrNull() ?: 0.0
                )

                viewModel.insertar(videojuego)

                navController.navigate(Routes.Principal.route) {
                    popUpTo(Routes.Principal.route) {
                        inclusive = true
                    }
                }
            }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text("Guardar")
    }

    Button(onClick = {
        navController.navigate(Routes.Principal.route)

    }) {
        Text(text = "Cancelar")
    }
}
