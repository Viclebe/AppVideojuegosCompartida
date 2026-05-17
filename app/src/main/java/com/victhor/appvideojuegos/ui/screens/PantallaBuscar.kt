package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.theme.AcentoNeonCyan
import com.victhor.appvideojuegos.ui.theme.AcentoNeonMagenta
import com.victhor.appvideojuegos.viewmodel.BuscarViewModel

@Composable
fun PantallaBuscar(
    navController: NavController,
    viewModel: BuscarViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = AcentoNeonMagenta)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Buscar en tu biblioteca",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AcentoNeonMagenta,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = uiState.textoBusqueda,
                onValueChange = { viewModel.cambiarTexto(it) },
                label = { Text("Buscar por título, género o plataforma") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.resultados.isEmpty()) {
                Text("No hay resultados")
            } else {
                LazyColumn {
                    items(uiState.resultados) { videojuego ->
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Routes.Detalle.route + "/${videojuego.firestoreId}")
                                }
                                .padding(12.dp)
                        ) {
                            Text(
                                text = videojuego.titulo,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                            )
                            
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = "${videojuego.genero} · ${videojuego.plataforma}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                if (!videojuego.estado.isNullOrBlank()) {
                                    val colorEstado = when (videojuego.estado) {
                                        "Finalizado" -> Color(0xFF4CAF50)
                                        "Jugando" -> Color(0xFF2196F3)
                                        "Pendiente" -> Color(0xFFFF9800)
                                        else -> Color.Gray
                                    }
                                    Surface(
                                        color = colorEstado.copy(alpha = 0.1f),
                                        shape = androidx.compose.foundation.shape.RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = videojuego.estado!!,
                                            color = colorEstado,
                                            style = MaterialTheme.typography.labelSmall,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                                            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                                        )
                                    }
                                }
                            }
                            HorizontalDivider(modifier = Modifier.padding(top = 8.dp), thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}