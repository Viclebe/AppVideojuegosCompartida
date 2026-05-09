package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.victhor.appvideojuegos.sesion.Sesion
import androidx.compose.material.icons.filled.Favorite
import com.victhor.appvideojuegos.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    navController: NavController,
    viewModel: DetalleViewModel,
    id: String
) {
    val uiState by viewModel.uiState.collectAsState()

    // Cargar los datos al entrar
    LaunchedEffect(id) {
        viewModel.cargarVideojuego(id)
    }

    var mostrarDialogo by remember { mutableStateOf(false) }
    var comentario by remember { mutableStateOf("") }
    var valoracion by remember { mutableIntStateOf(0) }

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

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = vj.titulo,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.weight(1f)
                    )
                    Image(
                        painter = painterResource(id = R.drawable.avatar),
                        contentDescription = vj.titulo,
                        modifier = Modifier
                            .width(110.dp)
                            .height(120.dp),
                        contentScale = ContentScale.Crop
                    )
                }

                // Mostrar LIKES
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal=16.dp)
                ) {
                    val haDadoLike = vj.likes.contains(Sesion.usuarioId)
                    IconButton(onClick = { viewModel.pulsarLike(vj.firestoreId) }) { // ViewModel
                        Icon(
                            imageVector = if (haDadoLike) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                            contentDescription = "Dar Like",
                            tint = if (haDadoLike) Color.Red else MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "${vj.likes.size} Likes",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Mostrar Videojuego
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        InsertarIcono(Icons.Filled.Category, "Género: ${vj.genero}")
                        InsertarIcono(Icons.Filled.VideogameAsset, "Plataforma: ${vj.plataforma}")
                        InsertarIcono(Icons.Filled.Star, "Valoración: ${vj.valoracion}")
                        InsertarIcono(Icons.Filled.Timeline, "Estado: ${vj.estado}")

                    }
                }

                // Mostrar los comentarios asociados al videojuego
                LazyColumn(modifier = Modifier.weight(1f)) {
                    item {
                        Text(
                            "Comentarios",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    items(uiState.comentarios) { comentarioLista ->
                        val fechaComent = SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale.getDefault()
                        ).format(Date(comentarioLista.fechaComentario))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = comentarioLista.nombreUsuario.ifBlank { "Usuario Oculto" },
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = comentarioLista.texto,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = fechaComent,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                // Escribir un nuevo comentario
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = comentario,
                        onValueChange = { comentario = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Escribe un comentario...") },
                        maxLines = 3
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (comentario.isNotBlank()) {
                                viewModel.enviarComentario(comentario, vj.firestoreId)
                                comentario = ""
                            }
                        }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Enviar")
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
