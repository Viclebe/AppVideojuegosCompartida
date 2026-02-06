package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.victhor.appvideojuegos.navigation.Routes




@Composable
fun PantallaDetalle(
    navController: NavController,
    viewModel: VideojuegoViewModel,
    id: Int
) {
    val videojuego by viewModel
        .buscarVideojuegoPorId(id)
        .observeAsState()

    videojuego?.let {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Título: ${it.titulo}")
            Text("Género: ${it.genero}")
            Text("Plataforma: ${it.plataforma}")
            Text("Estado: ${it.estado}")
            Text("Horas jugadas: ${it.horasJugadas}")
            Text("Valoración: ${it.valoracion}")

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = {
                navController.navigate(
                    Routes.Modificar.route + "/${it.id}"
                )
            }) {
                Text("Modificar")
            }

            Button(onClick = {
                viewModel.eliminar(it)
                navController.popBackStack()
            }) {
                Text("Eliminar")
            }
        }
    }
}
