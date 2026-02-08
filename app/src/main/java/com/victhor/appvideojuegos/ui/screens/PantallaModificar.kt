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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.text.input.KeyboardType


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

        //Las variables se inicializan con los valores actuales
        var titulo by remember { mutableStateOf(original.titulo) }
        var genero by remember { mutableStateOf(original.genero) }
        var plataforma by remember { mutableStateOf(original.plataforma) }
        var estado by remember { mutableStateOf(original.estado) }
        var horasJugadas by remember { mutableStateOf(original.horasJugadas.toString()) }
        var valoracion by remember {
            mutableStateOf(original.valoracion.toString())
        }
        // var descripcion by remember { mutableStateOf(original.descripcion) }


        //Variables para las validaciones
        var errorHoras by remember { mutableStateOf(false) }
        var errorValoracion by remember { mutableStateOf(false) }
        var errorEstado by remember { mutableStateOf(false) }
        val estadosCorrectos = listOf("Jugando", "Pendiente", "Finalizado")
        val hayErrores = errorHoras || errorValoracion || errorEstado


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            Text(
                text = "Modificar videojuego",
                style = MaterialTheme.typography.headlineSmall
            )

            //Campos para editar
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
                label = { Text("Estado") },
                modifier = Modifier.fillMaxWidth()
            )

            //Validación Estado
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
            //Validación horas
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
                label = { Text("Valoración (0.0 - 5.0") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            //Validación valoración
            if (errorValoracion) {
                Text(
                    text = "La valoración debe estar entre 0.0 y 5.0",
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            /* OutlinedTextField(
                 value = descripcion,
                 onValueChange = { descripcion = it },
                 label = { Text("Descripción") },
                 modifier = Modifier.fillMaxWidth()
             )*/

            Spacer(modifier = Modifier.height(8.dp))

            //Cambiar los campos editados
            Button(
                onClick = {
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
                },
                enabled = !hayErrores,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Guardar cambios")
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