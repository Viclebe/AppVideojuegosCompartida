package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.ui.theme.*
import com.victhor.appvideojuegos.viewmodel.ComunidadViewModel


@Composable
fun PantallaComunidad(
    navController: NavController,
    viewModel: ComunidadViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarVideojuegos() // Podrías llamar a una función específica de comunidad si la tienes
    }

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantallaNegro)
                .padding(16.dp)
        ) {
            //Cabecera
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = null,
                        tint = AcentoNeonMagenta
                    )
                }
                Text(
                    text = "Mundo Comunidad",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AcentoNeonMagenta,
                    letterSpacing = 2.sp
                )
                Spacer(modifier = Modifier.size(48.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            // FILTROS
            var expanded by remember { mutableStateOf(false) }
            Text(
                text = "FILTRAR POR ESTADO",
                style = MaterialTheme.typography.labelSmall,
                color = TextoSecundarioGris
            )
            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = true },
                    shape = RoundedCornerShape(4.dp),
                    color = FondoContenedoresOscuro,
                    border = BorderStroke(1.dp, AcentoNeonMagenta.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = uiState.filtroEstado?.uppercase() ?: "TODOS LOS REGISTROS",
                            color = AcentoNeonMagenta,
                            fontWeight = FontWeight.Bold
                        )
                        Icon(
                            Icons.Default.ArrowDropDown,
                            contentDescription = null,
                            tint = AcentoNeonMagenta
                        )
                    }
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .background(FondoContenedoresOscuro)
                        .border(1.dp, AcentoNeonMagenta, RoundedCornerShape(4.dp))
                ) {
                    val opciones = listOf(null, "Jugando", "Pendiente", "Finalizado")
                    opciones.forEach { opcion ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    opcion?.uppercase() ?: "VER TODO",
                                    color = if (uiState.filtroEstado == opcion) AcentoNeonMagenta else TextoPrincipalBlanco
                                )
                            },
                            onClick = { viewModel.cambiarFiltroEstado(opcion); expanded = false }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Listado de juegos
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = AcentoNeonMagenta)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(uiState.listaVideojuegos) { videojuego ->
                        val colorBorde = when (videojuego.estado) {
                            "Finalizado" -> EstadoNeonVerde
                            "Jugando" -> EstadoNeonAmarillo
                            else -> AcentoNeonBlue
                        }

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Routes.Detalle.route + "/${videojuego.firestoreId}")
                                },
                            shape = RoundedCornerShape(0.dp),
                            colors = CardDefaults.cardColors(containerColor = FondoContenedoresOscuro),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .drawBehind {
                                        drawRect(
                                            color = colorBorde,
                                            size = size.copy(width = 4.dp.toPx())
                                        )
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Miniatura del Juego
                                Box(
                                    modifier = Modifier
                                        .size(80.dp)
                                        .border(1.dp, colorBorde, RoundedCornerShape(4.dp))
                                        .padding(2.dp)
                                ) {
                                    AsyncImage(
                                        // URL o el avatar por defecto
                                        model = if (videojuego.imagenUrl.isNullOrBlank()) R.drawable.avatar else videojuego.imagenUrl,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(2.dp)),
                                        contentScale = ContentScale.Crop
                                    )
                                }

                                Column(modifier = Modifier
                                    .padding(start = 16.dp)
                                    .weight(1f)) {
                                    Text(
                                        text = videojuego.titulo.uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = TextoPrincipalBlanco
                                    )
                                    // Publicado por
                                    Text(
                                        text = "USUARIO: ${videojuego.nombreUsuario}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = colorBorde
                                    )
                                    Text(
                                        text = "PLATAFORMA: ${videojuego.plataforma.uppercase()}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = TextoSecundarioGris
                                    )

                                    // Estrellas de valoración
                                    Row(modifier = Modifier.padding(top = 8.dp)) {
                                        repeat(5) { index ->
                                            Icon(
                                                imageVector = Icons.Default.Star,
                                                contentDescription = null,
                                                tint = if (index < videojuego.valoracion.toInt()) EstadoNeonAmarillo else TextoSecundarioGris.copy(
                                                    alpha = 0.3f
                                                ),
                                                modifier = Modifier.size(12.dp)
                                            )
                                        }
                                    }
                                }

                                // Contador de Likes al final de la tarjeta
                                if (videojuego.likes.isNotEmpty()) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(
                                            Icons.Default.ThumbUp,
                                            contentDescription = null,
                                            tint = AcentoNeonCyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Text(
                                            "${videojuego.likes.size}",
                                            color = AcentoNeonCyan,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
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
