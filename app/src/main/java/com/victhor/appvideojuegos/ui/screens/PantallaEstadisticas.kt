package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.EstadisticasUiState
import com.victhor.appvideojuegos.viewmodel.EstadisticasViewModel

@Composable
fun PantallaEstadisticas(
    navController: NavController,
    viewModel: EstadisticasViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

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
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Estadísticas",
                    style = MaterialTheme.typography.headlineMedium
                )

                Card {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Total videojuegos: ${uiState.total}")
                        Text("Jugando: ${uiState.jugando}")
                        Text("Pendientes: ${uiState.pendientes}")
                        Text("Finalizados: ${uiState.finalizados}")
                        Text("Media valoración: %.2f".format(uiState.mediaValoracion))
                        Text("Horas totales: ${uiState.horasTotales}")
                    }
                }

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Volver")
                }
            }
        }
    }
}