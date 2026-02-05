package com.victhor.appvideojuegos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel
import com.victhor.appvideojuegos.ui.screens.*

@Composable
fun Navigation(videojuegoViewModel: VideojuegoViewModel) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        composable(Routes.Splash.route) {
            PantallaSplash(navController)
        }

        composable(Routes.Onboarding.route) {
            PantallaOnboarding(navController)
        }

        composable(Routes.Principal.route) {
            PantallaPrincipal(navController, videojuegoViewModel)
        }

        composable(Routes.Insertar.route) {
            PantallaInsertar(navController, videojuegoViewModel)
        }

        composable(
            route = Routes.Modificar.route + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")

            PantallaModificar(
                navController = navController,
                viewModel = videojuegoViewModel,
                id = id
            )
        }

        composable(
            route = Routes.Detalle.route + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")
            PantallaDetalle(
                navController = navController,
                viewModel = videojuegoViewModel,
                id = id
            )
        }

        composable(Routes.Estadisticas.route) {
            PantallaEstadisticas(navController, videojuegoViewModel)
        }

        composable(Routes.Ajustes.route) {
            PantallaAjustes(navController, videojuegoViewModel)
        }

        composable(Routes.Buscar.route) {
            PantallaBuscar(navController, videojuegoViewModel)
        }
    }
}
