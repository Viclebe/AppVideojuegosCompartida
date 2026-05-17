package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.DetalleViewModel
import com.victhor.appvideojuegos.navigation.Routes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.filled.Star
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.layout.ContentScale
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.unit.sp
import com.victhor.appvideojuegos.ui.theme.AcentoNeonBlue
import com.victhor.appvideojuegos.ui.theme.AcentoNeonCyan
import com.victhor.appvideojuegos.ui.theme.AcentoNeonMagenta
import com.victhor.appvideojuegos.ui.theme.FondoContenedoresOscuro
import com.victhor.appvideojuegos.ui.theme.FondoPantallaNegro
import com.victhor.appvideojuegos.ui.theme.TextoPrincipalBlanco
import com.victhor.appvideojuegos.ui.theme.TextoSecundarioGris
import com.victhor.appvideojuegos.ui.theme.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.material.icons.automirrored.filled.Send
import coil.compose.AsyncImage
import com.victhor.appvideojuegos.sesion.Sesion
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.material3.TextButton
import com.victhor.appvideojuegos.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaDetalle(
    navController: NavController,
    viewModel: DetalleViewModel,
    id: String
) {
    val uiState by viewModel.uiState.collectAsState()
    var nuevoComentario by remember { mutableStateOf("") }


    LaunchedEffect(id) {
        viewModel.cargarVideojuego(id)
    }

    AppScaffold {
        val videojuego = uiState.videojuego
        if (uiState.isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = AcentoNeonCyan)
            }
        } else if (videojuego != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(FondoPantallaNegro)
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen en la cabecera, ocupando todo
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ) {
                    // Imagen de fondo
                    AsyncImage(
                        model = if (videojuego.imagenUrl.isNullOrBlank()) R.drawable.avatar else videojuego.imagenUrl,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Capa de degradado para que el texto se lea bien
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, FondoPantallaNegro),
                                    startY = 100f
                                )
                            )
                    )

                    // Contenido encima de la imagen
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(20.dp)
                    ) {
                        Text(
                            text = videojuego.titulo,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.ExtraBold,
                            color = AcentoNeonCyan,
                            letterSpacing = 2.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = videojuego.plataforma,
                                color = AcentoNeonMagenta,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            // Badge de estado
                            Surface(
                                color = AcentoNeonCyan.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, AcentoNeonCyan),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = videojuego.estado?.uppercase() ?: "SIN ESTADO",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = AcentoNeonCyan
                                )
                            }
                        }
                    }

                }

                // --- 2. ESTADÍSTICAS Y NOTA ---
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "VALORACIÓN",
                            color = TextoSecundarioGris,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                null,
                                tint = EstadoNeonAmarillo,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                videojuego.valoracion.toString(),
                                color = EstadoNeonAmarillo,
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "GÉNERO",
                            color = TextoSecundarioGris,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Text(
                            videojuego.genero,
                            color = TextoPrincipalBlanco,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        val haDadoLike = videojuego.likes.contains(Sesion.usuarioId)
                        Text(
                            text = "${videojuego.likes.size} Likes",
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = { viewModel.pulsarLike(videojuego.firestoreId) }) { // ViewModel
                            Icon(
                                imageVector = if (haDadoLike) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                                contentDescription = "Dar Like",
                                tint = if (haDadoLike) Color.Red else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                // cOMENTARIOS
                Text(
                    text = "COMENTARIOS",
                    modifier = Modifier.padding(horizontal = 20.dp),
                    style = MaterialTheme.typography.labelMedium,
                    color = AcentoNeonCyan
                )

                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    uiState.comentarios.forEach { comentarioLista ->
                        // Formatear la fecha
                        val fechaComent = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .format(Date(comentarioLista.fechaComentario))

                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = FondoContenedoresOscuro,
                            border = BorderStroke(0.5.dp, TextoSecundarioGris.copy(alpha = 0.2f)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Row(modifier = Modifier
                                .drawBehind {
                                    drawRect(
                                        AcentoNeonCyan,
                                        size = size.copy(width = 2.dp.toPx())
                                    )
                                }
                                .padding(12.dp)) {
                                Column {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = comentarioLista.nombreUsuario.ifBlank { "Nombre_Usuario" },
                                            style = MaterialTheme.typography.labelSmall,
                                            color = AcentoNeonCyan
                                        )
                                        Text(
                                            text = fechaComent,
                                            style = MaterialTheme.typography.labelSmall,
                                            color = TextoSecundarioGris
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = comentarioLista.texto,
                                        color = TextoPrincipalBlanco,
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }

                // Comentarios
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = nuevoComentario,
                        onValueChange = { nuevoComentario = it },
                        modifier = Modifier.weight(1f),
                        placeholder = {
                            Text(
                                "Comenta...",
                                color = TextoSecundarioGris.copy(alpha = 0.5f)
                            )
                        },
                        shape = RoundedCornerShape(4.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AcentoNeonCyan,
                            unfocusedBorderColor = TextoSecundarioGris.copy(alpha = 0.3f),
                            focusedTextColor = TextoPrincipalBlanco
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            if (nuevoComentario.isNotBlank()) {
                                viewModel.enviarComentario(nuevoComentario, videojuego.firestoreId)
                                nuevoComentario = ""
                            }
                        },
                        modifier = Modifier.background(
                            AcentoNeonCyan.copy(alpha = 0.1f),
                            RoundedCornerShape(4.dp)
                        )
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.Send,
                            contentDescription = "Enviar",
                            tint = AcentoNeonCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                //modificar y eliminar pro solamente si coincide el usuario del videojuego y el tuyo
                if (videojuego.usuarioId == Sesion.usuarioId) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Button(
                            onClick = { navController.navigate(Routes.Modificar.route + "/${videojuego.firestoreId}") },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AcentoNeonBlue)
                        ) {
                            Text("MODIFICAR", fontWeight = FontWeight.Bold, color = Color.Black)
                        }

                        Button(
                            onClick = { viewModel.eliminarVideojuego(videojuego.firestoreId) },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(4.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = AlertaNeonRojo)
                        ) {
                            Text("ELIMINAR", fontWeight = FontWeight.Bold, color = Color.Black)
                        }
                    }
                }


                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("VOLVER A BIBLIOTECA", color = TextoSecundarioGris)
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
