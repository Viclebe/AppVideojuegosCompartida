package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.AjustesViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.Alignment
import com.victhor.appvideojuegos.ui.theme.*
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton

@Composable
fun PantallaAjustes(
    navController: NavController,
    viewModel: AjustesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantallaNegro)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    text = "Ajustes de la app",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AcentoNeonCyan,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.size(10.dp))

            // Eliminar  biblioteca entera
            Button(
                onClick = { viewModel.mostrarDialogoBorrar() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                border = BorderStroke(1.dp, Color.Red),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(Icons.Filled.Delete, contentDescription = null, tint = Color.Red)
                Spacer(modifier = Modifier.width(8.dp))
                Text("ELIMINAR BIBLIOTECA", color = Color.Red, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // --- SECCIÓN: SYSTEM_INFO ---
            Text(text = "INFO_SISTEMA", color = TextoSecundarioGris, fontSize = 12.sp)

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = FondoContenedoresOscuro,
                border = BorderStroke(1.dp, AcentoNeonBlue),
                shape = RoundedCornerShape(4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Fila de Versión
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "VERSIÓN", color = TextoSecundarioGris, fontSize = 12.sp)
                        Text(text = "7.0", color = AcentoNeonBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    // Fila de Desarrollador
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "DESARROLLADOR", color = TextoSecundarioGris, fontSize = 12.sp)
                        Text(text = "VÍCTOR LEÓN", color = AcentoNeonBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }

        if (uiState.mostrarDialogoBorrar) {
            AlertDialog(
                onDismissRequest = { viewModel.ocultarDialogoBorrar() },
                containerColor = FondoContenedoresOscuro,
                titleContentColor = Color.Red,
                textContentColor = TextoPrincipalBlanco,
                title = { Text("CONFIRMAR ELIMINAR BIBLIOTECA", fontWeight = FontWeight.Bold) },
                text = { Text("¿Seguro que deseas eliminar toda la biblioteca?") },
                confirmButton = {
                    TextButton(onClick = { viewModel.borrarBiblioteca() }) {
                        Text("Eliminar", color = Color.Red, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.ocultarDialogoBorrar() }) {
                        Text("Cancelar", color = AcentoNeonCyan)
                    }
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.border(1.dp, Color.Red, RoundedCornerShape(4.dp))
            )
        }
    }
}

