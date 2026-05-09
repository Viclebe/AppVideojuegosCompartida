package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.ComunidadViewModel
import com.victhor.appvideojuegos.viewmodel.ComunidadUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaComunidad(
    navController: NavController,
    viewModel: ComunidadViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Refrescar los juegos al entrar en esta pantalla para detectar sesión actual
    LaunchedEffect(Unit) {
        viewModel.cargarVideojuegos()
    }

    AppScaffold {
        Box(modifier = Modifier.fillMaxSize()) {

            ContenidoPantallaComunidad(
                uiState = uiState,
                navController = navController,
                onAlternarFavoritos = { viewModel.alternarFavoritos() },
                onCambiarFiltro = { viewModel.cambiarFiltroEstado(it) }
            )

            // FAB moderno
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.Insertar.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContenidoPantallaComunidad(
    uiState: ComunidadUiState,
    navController: NavController,
    onAlternarFavoritos: () -> Unit, // Avisar cuando pulsen favoritos
    onCambiarFiltro: (String?) -> Unit // Avisar cuando cambien el filtro
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Comunidad en UltimAppartida",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        // --- FILTROS ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilterChip(
                selected = uiState.soloFavoritos,
                onClick = { onAlternarFavoritos() },
                label = { Text("Favoritos") },
                leadingIcon = {
                    if (uiState.soloFavoritos) {
                        Icon(
                            Icons.Default.Star,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            )

            VerticalDivider(modifier = Modifier.height(24.dp))

            FilterChip(
                selected = uiState.filtroEstado == null,
                onClick = { onCambiarFiltro(null) },
                label = { Text("Todos") }
            )

            val estados = listOf("Jugando", "Pendiente", "Finalizado")
            estados.forEach { estado ->
                FilterChip(
                    selected = uiState.filtroEstado == estado,
                    onClick = { onCambiarFiltro(estado) },
                    label = { Text(estado) }
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error != null -> {
                Text(
                    text = uiState.error,
                    color = MaterialTheme.colorScheme.error
                )
            }


            uiState.listaVideojuegos.isEmpty() -> {
                Text("No hay videojuegos todavía")
            }

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.listaVideojuegos) { videojuego ->

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Routes.Detalle.route + "/${videojuego.firestoreId}"
                                    )
                                },
                            shape = RoundedCornerShape(20.dp),
                            elevation = CardDefaults.cardElevation(10.dp)
                        ) {

                            Row {

                                // 🎮 IMAGEN con estilo
                                Box {
                                    Image(
                                        painter = painterResource(id = R.drawable.avatar),
                                        contentDescription = videojuego.titulo,
                                        modifier = Modifier
                                            .width(110.dp)
                                            .height(120.dp),
                                        contentScale = ContentScale.Crop
                                    )

                                    // Overlay oscuro
                                    Box(
                                        modifier = Modifier
                                            .matchParentSize()
                                            .background(Color.Black.copy(alpha = 0.2f))
                                    )
                                }

                                Column(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth()
                                ) {

                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween, // Esto empuja el corazón al final
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = videojuego.titulo,
                                                style = MaterialTheme.typography.titleLarge,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Publicado por: ${videojuego.usuarioId}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color.Gray
                                            )
                                        }

                                        // Si el juego es favorito, pintamos el corazón
                                        if (videojuego.favorito) {
                                            Icon(
                                                imageVector = Icons.Default.Favorite,
                                                contentDescription = null,
                                                tint = Color.Red,
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    }

                                    if (!videojuego.estado.isNullOrBlank()) {
                                        val colorEstado = when (videojuego.estado) {
                                            "Finalizado" -> Color(0xFF4CAF50)
                                            "Jugando" -> Color(0xFF2196F3)
                                            "Pendiente" -> Color(0xFFFF9800)
                                            else -> Color.Gray
                                        }
                                        Surface(
                                            color = colorEstado.copy(alpha = 0.1f),
                                            shape = RoundedCornerShape(8.dp),
                                            modifier = Modifier.padding(vertical = 4.dp)
                                        ) {
                                            Text(
                                                text = videojuego.estado!!,
                                                color = colorEstado,
                                                style = MaterialTheme.typography.labelSmall,
                                                modifier = Modifier.padding(
                                                    horizontal = 8.dp,
                                                    vertical = 4.dp
                                                ),
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = Color(0xFFFFC107)
                                        )

                                        Spacer(modifier = Modifier.width(4.dp))

                                        Text(
                                            text = videojuego.valoracion.toString(),
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
