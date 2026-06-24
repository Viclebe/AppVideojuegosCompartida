package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.PerfilViewModel
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.theme.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import com.victhor.appvideojuegos.R

@Composable
fun PantallaPerfil(
    viewModel: PerfilViewModel,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsState()
    //Imagen
    var mostrarDialogoAvatar by remember { mutableStateOf(false) }
    var urlTemporal by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.cargarPerfil()
    }

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantallaNegro)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = AcentoNeonCyan)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Perfil",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AcentoNeonCyan,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.size(30.dp))

            if (uiState.isLoading) {
                CircularProgressIndicator(color = AcentoNeonCyan)
            } else {
                // Card con la info del usuario
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = FondoContenedoresOscuro,
                    border = BorderStroke(1.dp, AcentoNeonCyan),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .border(2.dp, AcentoNeonCyan, RoundedCornerShape(4.dp))
                                .background(Color.Black),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = if (uiState.avatarUrl.isNullOrBlank()) R.drawable.avatar else uiState.avatarUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(4.dp))
                                    .border(2.dp, AcentoNeonCyan, RoundedCornerShape(4.dp))
                                    .clickable { mostrarDialogoAvatar = true },
                                contentScale = ContentScale.Crop
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column {
                            Text(
                                text = "${uiState.nombreUsuario}",
                                color = TextoPrincipalBlanco,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "MAIL: ${if (uiState.email.length > 25) uiState.email.take(25) + "..." else uiState.email}",
                                color = AcentoNeonCyan,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "ESTADO: ONLINE",
                                color = EstadoNeonVerde,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Botón de Cerrar Sesión
                Button(
                    onClick = {
                        viewModel.cerrarSesion()
                        navController.navigate(Routes.Login.route) {
                            popUpTo(0)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, AcentoNeonMagenta),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text("CERRAR SESIÓN", color = AcentoNeonMagenta, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (mostrarDialogoAvatar) {
        AlertDialog(
            onDismissRequest = { mostrarDialogoAvatar = false },
            title = { Text("CAMBIAR_AVATAR", color = AcentoNeonCyan) },
            text = {
                OutlinedTextField(
                    value = urlTemporal,
                    onValueChange = { urlTemporal = it },
                    label = { Text("URL de la imagen") },
                    placeholder = { Text("https://...") }
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.actualizarAvatar(urlTemporal)
                    mostrarDialogoAvatar = false
                }) {
                    Text("GUARDAR", color = AcentoNeonCyan)
                }
            },
            containerColor = FondoContenedoresOscuro
        )
    }
}
