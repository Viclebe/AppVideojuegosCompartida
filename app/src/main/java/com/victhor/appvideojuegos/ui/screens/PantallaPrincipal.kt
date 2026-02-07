package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.text.font.FontWeight


@Composable
fun PantallaPrincipal(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    AppScaffold {
        ContenidoPantallaPrincipal(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun ContenidoPantallaPrincipal(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    val listaVideojuegos by viewModel.listaVideojuegos.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Mi biblioteca 🎮",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (listaVideojuegos.isEmpty()) {
            Text("No hay videojuegos todavía")
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(listaVideojuegos) { videojuego ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                navController.navigate(
                                    Routes.Detalle.route + "/${videojuego.id}"
                                )
                            }
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(videojuego.titulo, fontWeight = FontWeight.Bold)
                            Text("${videojuego.estado} · ⭐ ${videojuego.valoracion}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                navController.navigate(Routes.Buscar.route)
            }) {
                Text("Buscar")
            }

            Button(onClick = {
                navController.navigate(Routes.Estadisticas.route)
            }) {
                Text("Perfil")
            }

            Button(onClick = {
                navController.navigate(Routes.Ajustes.route)
            }) {
                Text("Ajustes")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                navController.navigate(Routes.Insertar.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("➕ Añadir videojuego")
        }
    }
}
