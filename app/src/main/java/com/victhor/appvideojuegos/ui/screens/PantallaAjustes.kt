package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material.icons.filled.Delete


@Composable
fun PantallaAjustes(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    AppScaffold {
        ContenidoPantallaAjustes(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun ContenidoPantallaAjustes(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    //Mostrar/ocultar dialogo de confirmación
    var mostrarDialogo by remember { mutableStateOf(false) }

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
                checked = viewModel.modoOscuro,
                onCheckedChange = {
                    viewModel.cambiarModoOscuro()
                }
            )

        }

        // Botón de borrar biblioteca
        Button(
            onClick = { mostrarDialogo = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            )
        ) {
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = "Eliminar"
            )
            Text("Borrar toda la biblioteca")
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Información de la app
        Text(
            text = "Información de la App",
            style = MaterialTheme.typography.headlineMedium,
        )
        val version = LocalContext.current.packageManager.getPackageInfo(
            LocalContext.current.packageName, 0
        ).versionName
        Text("Versión: $version")
        Text("Desarrollador: Víctor León Benedicto")

        Button(
            onClick = { navController.popBackStack() },
        ) {
            Text("Volver")
        }
    }

    // Confirmación borrar biblioteca
    if (mostrarDialogo) {
        AlertDialog(
            onDismissRequest = { mostrarDialogo = false },
            title = { Text("Confirmación") },
            text = {
                Text("¿Seguro que quieres borrar toda la biblioteca? Esta acción no se puede deshacer.")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.borrarBiblioteca()
                        mostrarDialogo = false
                    }
                ) {
                    Text("Borrar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { mostrarDialogo = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }

}
