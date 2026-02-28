package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import com.victhor.appvideojuegos.viewmodel.ModificarViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize




@Composable
fun PantallaModificar(
    navController: NavController,
    viewModel: ModificarViewModel,
    id: Int
) {

    val uiState by viewModel.uiState.collectAsState()

    // Cargar datos al entrar
    LaunchedEffect(id) {
        viewModel.cargarVideojuego(id)
    }

    // Volver atrás cuando se guarde
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
                    text = "Modificar videojuego",
                    style = MaterialTheme.typography.headlineSmall
                )

                OutlinedTextField(
                    value = uiState.titulo,
                    onValueChange = viewModel::guardarCambiosTitulo,
                    label = { Text("Título") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.genero,
                    onValueChange = viewModel::guardarCambiosGenero,
                    label = { Text("Género") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.plataforma,
                    onValueChange = viewModel::guardarCambiosPlataforma,
                    label = { Text("Plataforma") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.estado,
                    onValueChange = viewModel::guardarCambiosEstado,
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
                    onValueChange = viewModel::guardarCambiosHorasJugadas,
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
                    onValueChange = viewModel::guardarCambiosValoracion,
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
                    onClick = { viewModel.guardar(id) },
                    enabled = !uiState.errorHoras &&
                            !uiState.errorValoracion &&
                            !uiState.errorEstado,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Guardar cambios")
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