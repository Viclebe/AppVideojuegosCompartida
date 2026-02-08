package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment


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
fun ContenidoPantallaEstadísticas(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    //Recalcular automáticamente los valores
    val total by viewModel.totalVideojuegos.observeAsState(0)
    val jugando by viewModel.jugando.observeAsState(0)
    val pendientes by viewModel.pendientes.observeAsState(0)
    val finalizados by viewModel.finalizados.observeAsState(0)
    val media by viewModel.mediaValoracion.observeAsState(0.0)
    val horas by viewModel.horasTotales.observeAsState(0)

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

        //Card con las estadísticas
        Card {
            Column(
                Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text("Total videojuegos")
                Text("$total")

                Text("Jugando")
                Text("$jugando")

                Text("Pendientes")
                Text("$pendientes")

                Text("Finalizados")
                Text("$finalizados")

                Text("Media valoración")
                Text("%.2f".format(media))

                Text("Horas totales")
                Text("$horas")
            }
        }

        //Botón para volver
        Button(
            onClick = { navController.popBackStack() },
        ) {
            Text("Volver")
        }
    }
}

