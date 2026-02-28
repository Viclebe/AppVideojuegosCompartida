package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.InsertarUiState
import com.victhor.appvideojuegos.viewmodel.InsertarViewModel

@Composable
fun PantallaInsertar(
    navController: NavController,
    viewModel: InsertarViewModel
) {
    val uiState by viewModel.uiState.collectAsState(initial = InsertarUiState())

    // Navegar atrás cuando se guarde
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) {
            navController.popBackStack()
        }
    }

    AppScaffold {
        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Agregar nuevo videojuego",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = uiState.titulo,
                    onValueChange = viewModel::cambiarTitulo,
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.genero,
                    onValueChange = viewModel::cambiarGenero,
                    label = { Text("Género") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.plataforma,
                    onValueChange = viewModel::cambiarPlataforma,
                    label = { Text("Plataforma") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.estado,
                    onValueChange = viewModel::cambiarEstado,
                    label = { Text("Estado") },
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.errorEstado) {
                    Text(
                        text = "Estado inválido. Usa: Jugando, Pendiente o Finalizado",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = uiState.horasJugadas,
                    onValueChange = viewModel::cambiarHorasJugadas,
                    label = { Text("Horas jugadas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.errorHoras) {
                    Text(
                        text = "Las horas deben ser 0 o mayores",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                OutlinedTextField(
                    value = uiState.valoracion,
                    onValueChange = viewModel::cambiarValoracion,
                    label = { Text("Valoración (0.0 - 5.0)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                if (uiState.errorValoracion) {
                    Text(
                        text = "La valoración debe estar entre 0.0 y 5.0",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = viewModel::guardar,
                    enabled = !uiState.errorHoras && !uiState.errorValoracion && !uiState.errorEstado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar")
                }

                TextButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar")
                }
            }
        }
    }
}