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
import com.victhor.appvideojuegos.viewmodel.LoginViewModel
import com.victhor.appvideojuegos.viewmodel.ModificarViewModel
import com.victhor.appvideojuegos.viewmodel.PerfilViewModel
import com.victhor.appvideojuegos.viewmodel.PrincipalViewModel

/**
 * Función para gestionar la navegación entre pantallas usando Jetpack Compose Navigation.
 * Todas las rutas especifican la pantalla y qué ViewModel gestiona su lógica.
 * Composable declara una ruta de navegación en Jetpack Compose.
 */
@Composable
fun Navigation( // declarar ViewModels
    principalViewModel: PrincipalViewModel,
    modificarViewModel: ModificarViewModel,
    insertarViewModel: InsertarViewModel,
    estadisticasViewModel: EstadisticasViewModel,
    detalleViewModel: DetalleViewModel,
    buscarViewModel: BuscarViewModel,
    ajustesViewModel: AjustesViewModel,
    loginViewModel: LoginViewModel,
    perfilViewModel: PerfilViewModel
) {

    //Es un controlador de navegación que permite cambiar entre pantallas
    val navController = rememberNavController()

    //Contenedor principal de la navegación (Controlador + pantalla inicial)
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route
    ) {

        // SPLASH
        composable(Routes.Splash.route) {
            PantallaSplash(navController)
        }

        // ONBOARDING
        composable(Routes.Onboarding.route) {
            PantallaOnboarding(navController)
        }

        // PRINCIPAL
        composable(Routes.Principal.route) {
            PantallaPrincipal(navController, principalViewModel)
        }

        // INSERTAR
        composable(Routes.Insertar.route) {
            PantallaInsertar(navController, insertarViewModel)
        }

        // MODIFICACIÓN por parámetro Id del videojuego
        composable(
            route = Routes.Modificar.route + "/{id}", // La ruta varía según el id que recibe
            arguments = listOf( // Lista de parámetros que espera la ruta
                navArgument("id") {
                    type = NavType.IntType
                } // Tipo de argumento que será el id (Int)
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

        // ESTADÍSTICAS
        composable(Routes.Estadisticas.route) {
            PantallaEstadisticas(navController, estadisticasViewModel)
        }

        // AJUSTES
        composable(Routes.Ajustes.route) {
            PantallaAjustes(navController, ajustesViewModel)
        }

        // BUSCAR
        composable(Routes.Buscar.route) {
            PantallaBuscar(navController, buscarViewModel)
        }

        //LOGIN
        composable(Routes.Login.route) {
            PantallaLogin(navController, loginViewModel)
        }

        //PERFIL
        composable(Routes.Perfil.route) {
            PantallaPerfil(perfilViewModel, navController)
        }
    }
}
