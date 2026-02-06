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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel


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
fun ContenidoPantallaPrincipal(navController: NavController, viewModel: VideojuegoViewModel) {
    val listaVideojuegos by viewModel.listaVideojuegos.observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (listaVideojuegos.isEmpty()) {
            Text("No hay videojuegos")
        } else {
            LazyColumn {
                items(listaVideojuegos) { videojuego ->
                    Text(
                        text = "${videojuego.titulo} / ${videojuego.estado} / ${videojuego.valoracion}",
                        modifier = Modifier
                            .padding(16.dp)
                            .clickable {
                                navController.navigate(
                                    Routes.Detalle.route + "/${videojuego.id}"
                                )
                            })
                }
            }
        }
        Button(onClick = {
            navController.navigate(Routes.Insertar.route)
        }) {
            Text(text = "+")
        }

        Button(onClick = {
            navController.navigate(Routes.Buscar.route)

        }) {
            Text(text = "Buscar")
        }
        Button(onClick = {
            navController.navigate(Routes.Estadisticas.route)

        }) {
            Text(text = "Perfil")
        }
        Button(onClick = {
            navController.navigate(Routes.Ajustes.route)

        }) {
            Text(text = "Ajustes")
        }

    }
}
