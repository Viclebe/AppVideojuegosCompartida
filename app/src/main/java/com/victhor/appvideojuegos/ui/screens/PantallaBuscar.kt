package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.viewmodel.BuscarViewModel

@Composable
fun PantallaBuscar(
    navController: NavController,
    viewModel: BuscarViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Text(
                text = "Buscar videojuegos",
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = uiState.textoBusqueda,
                onValueChange = { viewModel.cambiarTexto(it) },
                label = { Text("Buscar por título, género, plataforma o estado") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (uiState.resultados.isEmpty()) {
                Text("No hay resultados")
            } else {
                LazyColumn {
                    items(uiState.resultados) { videojuego ->
                        Text(
                            text = "${videojuego.titulo} · ${videojuego.genero} · ${videojuego.plataforma} · ${videojuego.estado}",
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp)
                                .clickable {
                                    navController.navigate(Routes.Detalle.route + "/${videojuego.id}")
                                }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Volver")
            }
        }
    }
}