package com.victhor.appvideojuegos.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.victhor.appvideojuegos.ui.screens.*
import com.victhor.appvideojuegos.ui.screens.PantallaPrincipal
import com.victhor.appvideojuegos.viewmodel.AjustesViewModel
import com.victhor.appvideojuegos.viewmodel.BuscarViewModel
import com.victhor.appvideojuegos.viewmodel.DetalleViewModel
import com.victhor.appvideojuegos.viewmodel.EstadisticasViewModel
import com.victhor.appvideojuegos.viewmodel.InsertarViewModel
import com.victhor.appvideojuegos.viewmodel.ModificarViewModel
import com.victhor.appvideojuegos.viewmodel.PrincipalViewModel

@Composable
fun Navigation(
    principalViewModel: PrincipalViewModel,
    modificarViewModel: ModificarViewModel,
    insertarViewModel: InsertarViewModel,
    estadisticasViewModel: EstadisticasViewModel,
    detalleViewModel: DetalleViewModel,
    buscarViewModel: BuscarViewModel,
    ajustesViewModel: AjustesViewModel
) {

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
            PantallaPrincipal(navController, principalViewModel)
        }

        composable(Routes.Insertar.route) {
            PantallaInsertar(navController, insertarViewModel)
        }

        //Pantalla de modificación con parámetro Id
        composable(
            route = Routes.Modificar.route + "/{id}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments!!.getInt("id")

            PantallaModificar(
                navController = navController,
                viewModel = modificarViewModel,
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
                viewModel = detalleViewModel,
                id = id
            )
        }

        composable(Routes.Estadisticas.route) {
            PantallaEstadisticas(navController, estadisticasViewModel)
        }

        composable(Routes.Ajustes.route) {
            PantallaAjustes(navController, ajustesViewModel)
        }

        composable(Routes.Buscar.route) {
            PantallaBuscar(navController, buscarViewModel)
        }
    }
}
