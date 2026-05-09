package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.InsertarUiState
import com.victhor.appvideojuegos.viewmodel.InsertarViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInsertar(
    navController: NavController,
    viewModel: InsertarViewModel
) {
    val uiState by viewModel.uiState.collectAsState(initial = InsertarUiState())

    // Navegar atrás cuando se guarde
    LaunchedEffect(uiState.guardadoExitoso) {
        if (uiState.guardadoExitoso) {
            navController.popBackStack() // Salir
            viewModel.reiniciarGuardadoExitoso() // Reiniciar el estado GuardadoExitoso de true a false
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
                    value = uiState.valoracion,
                    onValueChange = viewModel::cambiarValoracion,
                    label = { Text("Valoración (0-5)") },
                    isError = uiState.errorValoracion,
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

                // --- NUEVO: Estado ---
                var expanded by remember { mutableStateOf(false) }
                val opcionesEstado = listOf("Jugando", "Pendiente", "Finalizado")

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded }
                ) {
                    OutlinedTextField(
                        value = uiState.estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier.menuAnchor().fillMaxWidth()
                    )
                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opcionesEstado.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion) },
                                onClick = {
                                    viewModel.cambiarEstado(opcion)
                                    expanded = false
                                }
                            )
                        }
                    }
                }

                // --- NUEVO: Horas Jugadas ---
                OutlinedTextField(
                    value = uiState.horasJugadas,
                    onValueChange = viewModel::cambiarHoras,
                    label = { Text("Horas Jugadas") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    isError = uiState.errorHoras
                )
                if (uiState.errorHoras) {
                    Text(
                        text = "Introduce un número válido de horas (mínimo 0)",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = {
                        viewModel.guardar()
                    },
                    enabled = !uiState.errorValoracion && !uiState.errorHoras,
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