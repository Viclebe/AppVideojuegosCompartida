package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.victhor.appvideojuegos.ui.layout.AppScaffold


@Composable
fun PantallaModificar(
    navController: NavController,
    viewModel: VideojuegoViewModel,
    id: Int
) {
    AppScaffold {
        ContenidoPantallaModificar(
            navController = navController,
            viewModel = viewModel,
            id = id
        )
    }
}

@Composable
fun ContenidoPantallaModificar(
    navController: NavController,
    viewModel: VideojuegoViewModel,
    id: Int
) {
    val videojuego by viewModel
        .buscarVideojuegoPorId(id)
        .observeAsState()

    videojuego?.let { original ->

        var titulo by remember { mutableStateOf(original.titulo) }
        var genero by remember { mutableStateOf(original.genero) }
        var plataforma by remember { mutableStateOf(original.plataforma) }
        var estado by remember { mutableStateOf(original.estado) }
        var horasJugadas by remember { mutableStateOf(original.horasJugadas.toString()) }
        var valoracion by remember {
            mutableStateOf(original.valoracion.toString())
        }

        Column(modifier = Modifier.padding(16.dp)) {

            OutlinedTextField(
                value = titulo,
                onValueChange = { titulo = it },
                label = { Text("Título") }
            )

            OutlinedTextField(
                value = genero,
                onValueChange = { genero = it },
                label = { Text("Género") }
            )

            OutlinedTextField(
                value = plataforma,
                onValueChange = { plataforma = it },
                label = { Text("Plataforma") }
            )

            OutlinedTextField(
                value = estado,
                onValueChange = { estado = it },
                label = { Text("Estado") }
            )

            OutlinedTextField(
                value = horasJugadas,
                onValueChange = { horasJugadas = it },
                label = { Text("Horas Jugadas") }
            )

            OutlinedTextField(
                value = valoracion,
                onValueChange = { valoracion = it },
                label = { Text("Valoración") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                viewModel.modificar(
                    original.copy(
                        titulo = titulo,
                        genero = genero,
                        plataforma = plataforma,
                        estado = estado,
                        horasJugadas = horasJugadas.toIntOrNull() ?: original.horasJugadas,
                        valoracion = valoracion.toDoubleOrNull() ?: original.valoracion
                    )
                )
                navController.popBackStack()
            }) {
                Text("Guardar cambios")
            }
        }
    }
}
