package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.AjustesViewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete


@Composable
fun PantallaAjustes(
    navController: NavController,
    viewModel: AjustesViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val version = try {
        context.packageManager.getPackageInfo(context.packageName, 0).versionName
    } catch (e: Exception) { "1.0" }

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Ajustes",
                style = MaterialTheme.typography.headlineMedium
            )

            // Switch modo oscuro
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Modo Oscuro")
                Switch(
                    checked = uiState.modoOscuro,
                    onCheckedChange = { viewModel.cambiarModoOscuro() }
                )
            }

            // Botón borrar biblioteca
            Button(
                onClick = { viewModel.mostrarDialogoBorrar() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Filled.Delete, contentDescription = "Eliminar")
                Spacer(modifier = Modifier.width(8.dp))
                Text("Borrar toda la biblioteca")
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información de la app
            Text(
                text = "Información de la App",
                style = MaterialTheme.typography.headlineMedium,
            )
            Text("Versión: $version")
            Text("Desarrollador: Víctor León Benedicto")

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }

        // Diálogo de confirmación
        if (uiState.mostrarDialogoBorrar) {
            AlertDialog(
                onDismissRequest = { viewModel.ocultarDialogoBorrar() },
                title = { Text("Confirmación") },
                text = {
                    Text("¿Seguro que quieres borrar toda la biblioteca? Esta acción no se puede deshacer.")
                },
                confirmButton = {
                    TextButton(onClick = { viewModel.borrarBiblioteca() }) {
                        Text("Borrar")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { viewModel.ocultarDialogoBorrar() }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}