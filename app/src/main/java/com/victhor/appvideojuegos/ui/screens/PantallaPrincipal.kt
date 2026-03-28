package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.viewmodel.PrincipalUiState
import com.victhor.appvideojuegos.viewmodel.PrincipalViewModel

@Composable
fun PantallaPrincipal(
    navController: NavController,
    viewModel: PrincipalViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AppScaffold {
        Box(modifier = Modifier.fillMaxSize()) {

            ContenidoPantallaPrincipal(
                uiState = uiState,
                navController = navController
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

@Composable
fun ContenidoPantallaPrincipal(
    uiState: PrincipalUiState,
    navController: NavController
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "🎮 Mi biblioteca",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

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

                        val colorEstado = when (videojuego.estado) {
                            "Completado" -> Color(0xFF4CAF50)
                            "Pendiente" -> Color(0xFFFF9800)
                            "Finalizado" -> Color(0xFF0056ff)
                            else -> Color.Gray
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(
                                        Routes.Detalle.route + "/${videojuego.id}"
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

                                    Text(
                                        text = videojuego.titulo,
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold
                                    )

                                    Spacer(modifier = Modifier.height(6.dp))

                                    // Estado tipo badge
                                    Box(
                                        modifier = Modifier
                                            .background(
                                                colorEstado.copy(alpha = 0.2f),
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .padding(horizontal = 8.dp, vertical = 4.dp)
                                    ) {
                                        Text(
                                            text = videojuego.estado,
                                            color = colorEstado,
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(8.dp))

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

        Spacer(modifier = Modifier.height(12.dp))

        // Botones mejorados (tipo chips)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            AssistChip(
                onClick = { navController.navigate(Routes.Buscar.route) },
                label = { Text("Buscar") }
            )

            AssistChip(
                onClick = { navController.navigate(Routes.Estadisticas.route) },
                label = { Text("Stats") }
            )

            AssistChip(
                onClick = { navController.navigate(Routes.Ajustes.route) },
                label = { Text("Ajustes") }
            )

            AssistChip(
                onClick = { navController.navigate(Routes.Perfil.route) },
                label = { Text("Perfil") }
            )
        }
    }
}