package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.material3.OutlinedTextField
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme


@Composable
fun PantallaBuscar(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    AppScaffold {
        ContenidoPantallaBuscar(
            navController = navController,
            viewModel = viewModel
        )
    }
}


@Composable
fun ContenidoPantallaBuscar(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    var texto by remember { mutableStateOf("") }

    //Observar los resultados
    val videojuegos by viewModel
        .buscar(texto)
        .observeAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        //Campo de búsqueda
        Text(
            text = "Buscar videojuegos",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Buscar por título, género, plataforma o estado") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (videojuegos.isEmpty()) {
            Text("No hay resultados")
        } else {
            LazyColumn {
                items(videojuegos) { videojuego ->
                    Text(
                        text = "${videojuego.titulo} · ${videojuego.genero} · ${videojuego.plataforma} · ${videojuego.estado}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                            .clickable {
                                //Navegar al detalle
                                navController.navigate(
                                    Routes.Detalle.route + "/${videojuego.id}"
                                )
                            }
                    )
                }
            }
        }
        Button(
            onClick = { navController.popBackStack() },
        ) {
            Text("Volver")
        }
    }
}