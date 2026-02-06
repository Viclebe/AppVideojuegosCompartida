package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier


@Composable
fun PantallaEstadisticas(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    AppScaffold {
        ContenidoPantallaEstadísticas(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun ContenidoPantallaEstadísticas(navController: NavController, viewModel: VideojuegoViewModel) {
    val total by viewModel.totalVideojuegos.observeAsState(0)
    val jugando by viewModel.jugando.observeAsState(0)
    val pendientes by viewModel.pendientes.observeAsState(0)
    val finalizados by viewModel.finalizados.observeAsState(0)
    val media by viewModel.mediaValoracion.observeAsState(0.0)
    val horas by viewModel.horasTotales.observeAsState(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Estadísticas")

        Spacer(modifier = Modifier.height(16.dp))

        Text("Total videojuegos: $total")
        Text("Jugando: $jugando")
        Text("Pendientes: $pendientes")
        Text("Finalizados: $finalizados")
        Text("Media valoración: ${"%.2f".format(media)}")
        Text("Horas totales jugadas: $horas")
    }
}