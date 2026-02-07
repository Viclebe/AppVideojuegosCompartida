package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
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

    var errorHoras by remember { mutableStateOf(false) }
    var errorValoracion by remember { mutableStateOf(false) }
    var errorEstado by remember { mutableStateOf(false) }
    val estadosCorrectos = listOf("Jugando", "Pendiente", "Finalizado")
    val hayErrores = errorHoras || errorValoracion || errorEstado

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {

            Text(
                text = "Añadir videojuego",
                style = MaterialTheme.typography.headlineMedium
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
                onValueChange = {
                    estado = it
                    errorEstado = it.isNotBlank() && it !in estadosCorrectos
                },
                label = { Text("Estado (Jugando / Pendiente / Finalizado)") },
                modifier = Modifier.fillMaxWidth()
            )

            if (errorEstado) {
                Text(
                    text = "Estado inválido. Prueba: Jugando, Pendiente o Finalizado",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = horasJugadas,
                onValueChange = {
                    horasJugadas = it
                    errorHoras = it.toIntOrNull()?.let { horas -> horas < 0 } ?: false
                },
                label = { Text("Horas jugadas") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            if (errorHoras) {
                Text(
                    text = "Las horas deben ser 0 o mayores",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            OutlinedTextField(
                value = valoracion,
                onValueChange = {
                    valoracion = it
                    errorValoracion = it.toDoubleOrNull()?.let { valor ->
                        valor < 0.0 || valor > 5.0
                    } ?: false
                },
                label = { Text("Valoración (0.0 – 5.0)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
        }

        if (errorValoracion) {
            Text(
                text = "La valoración debe estar entre 0.0 y 5.0",
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {

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
                            popUpTo(Routes.Principal.route) { inclusive = true }
                        }
                    }
                },
                enabled = !hayErrores,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                Text("Guardar")
            }

            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cancelar")
            }
        }
    }
}
