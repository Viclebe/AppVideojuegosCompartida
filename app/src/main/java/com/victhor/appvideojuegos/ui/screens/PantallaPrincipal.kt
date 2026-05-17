package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.viewmodel.PrincipalUiState
import com.victhor.appvideojuegos.viewmodel.PrincipalViewModel
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.unit.sp
import com.victhor.appvideojuegos.ui.theme.AcentoNeonBlue
import com.victhor.appvideojuegos.ui.theme.AcentoNeonCyan
import com.victhor.appvideojuegos.ui.theme.AcentoNeonMagenta
import com.victhor.appvideojuegos.ui.theme.EstadoNeonVerde
import com.victhor.appvideojuegos.ui.theme.FondoContenedoresOscuro
import com.victhor.appvideojuegos.ui.theme.FondoPantallaNegro
import com.victhor.appvideojuegos.ui.theme.TextoPrincipalBlanco
import com.victhor.appvideojuegos.ui.theme.TextoSecundarioGris
import androidx.compose.material.icons.Icons
import androidx.compose.ui.graphics.Color
import com.victhor.appvideojuegos.ui.theme.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    navController: NavController,
    viewModel: PrincipalViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    // Refrescar los juegos al entrar en esta pantalla para detectar sesión actual
    LaunchedEffect(Unit) {
        viewModel.cargarVideojuegos()
    }

    AppScaffold {
        Box(modifier = Modifier.fillMaxSize()) {

            ContenidoPantallaPrincipal(
                uiState = uiState,
                navController = navController,
                onCambiarFiltro = { viewModel.cambiarFiltroEstado(it) }
            )

            // FAB
            FloatingActionButton(
                onClick = {
                    navController.navigate(Routes.Insertar.route)
                },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                containerColor = AcentoNeonBlue,
                contentColor = Color.White
            ) {
                Icon(Icons.Default.Add, contentDescription = "Añadir")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContenidoPantallaPrincipal(
    uiState: PrincipalUiState,
    navController: NavController,
    onCambiarFiltro: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantallaNegro)
            .padding(16.dp)
    ) {
        // Título cabecera
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mi biblioteca",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = AcentoNeonCyan,
                letterSpacing = 2.sp
            )
            IconButton(onClick = { navController.navigate(Routes.Perfil.route) }) {
                Icon(Icons.Default.Person, contentDescription = null, tint = AcentoNeonCyan)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Fila de botones de navegación
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val navItems = listOf(
                Triple(Icons.Default.Search, AcentoNeonBlue, Routes.Buscar.route),
                Triple(Icons.Default.Share, AcentoNeonMagenta, Routes.Comunidad.route),
                Triple(Icons.Default.Settings, TextoSecundarioGris, Routes.Ajustes.route),
                Triple(Icons.Default.Info, EstadoNeonVerde, Routes.Estadisticas.route)
            )

            navItems.forEach { (icono, navColor, ruta) ->
                Surface(
                    modifier = Modifier
                        .size(50.dp)
                        .clickable { navController.navigate(ruta) },
                    shape = RoundedCornerShape(8.dp),
                    color = navColor.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        navColor.copy(alpha = 0.5f)
                    )
                ) {
                    Icon(
                        imageVector = icono,
                        contentDescription = null,
                        tint = navColor,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Filtros
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
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    AcentoNeonCyan.copy(alpha = 0.5f)
                )
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = uiState.filtroEstado?.uppercase() ?: "TODOS LOS JUEGOS",
                        color = AcentoNeonCyan,
                        fontWeight = FontWeight.Bold
                    )
                    Icon(
                        Icons.Default.ArrowDropDown,
                        contentDescription = null,
                        tint = AcentoNeonCyan
                    )
                }
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(FondoContenedoresOscuro)
                    .border(1.dp, AcentoNeonCyan, RoundedCornerShape(4.dp))
            ) {
                val opciones = listOf(null, "Jugando", "Pendiente", "Finalizado")
                opciones.forEach { opcion ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                opcion?.uppercase() ?: "TODOS",
                                color = if (uiState.filtroEstado == opcion) AcentoNeonCyan else TextoPrincipalBlanco
                            )
                        },
                        onClick = { onCambiarFiltro(opcion); expanded = false }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Juegos listado
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
                        .clickable { navController.navigate(Routes.Detalle.route + "/${videojuego.firestoreId}") },
                    shape = RoundedCornerShape(0.dp),
                    colors = CardDefaults.cardColors(containerColor = FondoContenedoresOscuro),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .drawBehind {
                                drawRect(color = colorBorde, size = size.copy(width = 4.dp.toPx()))
                            }
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .border(1.dp, colorBorde, RoundedCornerShape(4.dp))
                                .padding(2.dp)
                        ) {
                            AsyncImage(
                                //Mostrar el avatar por defecto si no hay imagen
                                model = if (videojuego.imagenUrl.isNullOrBlank()) R.drawable.avatar else videojuego.imagenUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(2.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }

                        Column(
                            modifier = Modifier
                                .padding(start = 16.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = videojuego.titulo.uppercase(),
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextoPrincipalBlanco
                            )
                            Text(
                                text = videojuego.plataforma.uppercase(),
                                style = MaterialTheme.typography.labelSmall,
                                color = colorBorde
                            )
                            Row(modifier = Modifier.padding(top = 8.dp)) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < videojuego.valoracion.toInt()) EstadoNeonAmarillo else TextoSecundarioGris.copy(
                                            alpha = 0.3f
                                        ),
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }
                        }
                        if (videojuego.favorito) {
                            Icon(
                                Icons.Default.Favorite,
                                contentDescription = null,
                                tint = AcentoNeonMagenta,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
