package com.victhor.appvideojuegos.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel

@Composable
fun PantallaFiltros(
    navController: NavController,
    viewModel: VideojuegoViewModel
) {
    AppScaffold {
        ContenidoPantallaFiltros(
            navController = navController,
            viewModel = viewModel
        )
    }
}

@Composable
fun ContenidoPantallaFiltros(navController: NavController, viewModel: VideojuegoViewModel) {
    Text("Filtros")
}