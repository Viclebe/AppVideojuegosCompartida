package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.DetalleViewModel
import com.victhor.appvideojuegos.navigation.Routes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun PantallaDetalle(
    navController: NavController,
    viewModel: DetalleViewModel,
    id: Int
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar los datos al entrar
    LaunchedEffect(id) {
        viewModel.cargarVideojuego(id)
    }

    var mostrarDialogo by remember { mutableStateOf(false) }

    AppScaffold {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.videojuego == null) {
            // Error o no encontrado
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(uiState.error ?: "Videojuego no encontrado")
            }
        } else {
            val vj = uiState.videojuego!!
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
                        InsertarIcono(Icons.Filled.Category, "Género: ${vj.genero}")
                        InsertarIcono(Icons.Filled.VideogameAsset, "Plataforma: ${vj.plataforma}")
                        InsertarIcono(Icons.Filled.Flag, "Estado: ${vj.estado}")
                        InsertarIcono(Icons.Filled.Schedule, "Horas jugadas: ${vj.horasJugadas}")
                        InsertarIcono(Icons.Filled.Star, "Valoración: ${vj.valoracion}")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(onClick = {
                        navController.navigate(Routes.Modificar.route + "/${vj.id}")
                    }) {
                        Text("Modificar")
                    }

                    Button(
                        onClick = { mostrarDialogo = true },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Text("Eliminar")
                    }
                }

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }
        }

        if (mostrarDialogo) {
            AlertDialog(
                onDismissRequest = { mostrarDialogo = false },
                title = { Text("Confirmación") },
                text = { Text("¿Seguro que quieres borrar este videojuego?") },
                confirmButton = {
                    TextButton(onClick = {
                        viewModel.eliminarVideojuego()
                        navController.popBackStack()
                    }) { Text("Borrar") }
                },
                dismissButton = {
                    TextButton(onClick = { mostrarDialogo = false }) { Text("Cancelar") }
                }
            )
        }
    }
}

@Composable
fun InsertarIcono(
    icono: ImageVector,
    texto: String
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icono, contentDescription = null)
        Spacer(modifier = Modifier.width(8.dp))
        Text(texto)
    }
}