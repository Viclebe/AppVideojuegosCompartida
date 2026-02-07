package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.TextButton
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.victhor.appvideojuegos.navigation.Routes
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import com.victhor.appvideojuegos.ui.layout.AppScaffold


@Composable
fun PantallaDetalle(
    navController: NavController,
    viewModel: VideojuegoViewModel,
    id: Int
) {
    AppScaffold {
        ContenidoPantallaDetalle(
            navController = navController,
            viewModel = viewModel,
            id = id
        )
    }
}

@Composable
fun ContenidoPantallaDetalle(
    navController: NavController,
    viewModel: VideojuegoViewModel,
    id: Int
) {
    //Observar un videojuego por id y actualizar automáticamente si cambia
    val videojuego by viewModel.buscarVideojuegoPorId(id).observeAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }

    videojuego?.let { vj ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = vj.titulo,
                style = MaterialTheme.typography.headlineMedium
            )

            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    //Card con los datos
                    Text("Género: ${vj.genero}")
                    Text("Plataforma: ${vj.plataforma}")
                    Text("Estado: ${vj.estado}")
                    Text("Horas jugadas: ${vj.horasJugadas}")
                    Text("Valoración: ${vj.valoracion}")
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                //Ruta a modificar
                Button(onClick = {
                    navController.navigate(
                        Routes.Modificar.route + "/${vj.id}"
                    )
                }) {
                    Text("Modificar")
                }

                //Botón para eliminar
                Button(
                    onClick = { mostrarDialogo = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Eliminar")
                }
            }

            //Botón para vovler
            OutlinedButton(
                onClick = { navController.popBackStack() },
            ) {
                Text("Volver")
            }
        }
    }

    //Pantalla de alerta al borrar
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Confirmación") },
            text = {
                Text("¿Seguro que quieres borrar este videojuego?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.eliminar(videojuego!!)
                        navController.popBackStack()
                    }
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarDialogo = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
