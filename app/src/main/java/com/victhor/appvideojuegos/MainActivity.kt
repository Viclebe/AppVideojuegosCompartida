package com.victhor.appvideojuegos

import com.victhor.appvideojuegos.navigation.Navigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.theme.AppVideojuegosTheme
import com.victhor.appvideojuegos.viewmodel.AjustesViewModel
import com.victhor.appvideojuegos.viewmodel.AppViewModelFactory
import com.victhor.appvideojuegos.viewmodel.BuscarViewModel
import com.victhor.appvideojuegos.viewmodel.DetalleViewModel
import com.victhor.appvideojuegos.viewmodel.EstadisticasViewModel
import com.victhor.appvideojuegos.viewmodel.InsertarViewModel
import com.victhor.appvideojuegos.viewmodel.ModificarViewModel
import com.victhor.appvideojuegos.viewmodel.PrincipalViewModel
import com.victhor.appvideojuegos.viewmodel.LoginViewModel
import com.victhor.appvideojuegos.viewmodel.PerfilViewModel


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // Instanciamos nuestro Factory pasándole la Application
            val factory = AppViewModelFactory(application)


            val principalViewModel: PrincipalViewModel = viewModel(factory = factory)
            val modificarViewModel: ModificarViewModel = viewModel(factory = factory)
            val insertarViewModel: InsertarViewModel = viewModel(factory = factory)
            val estadisticasViewModel: EstadisticasViewModel = viewModel(factory = factory)
            val detalleViewModel: DetalleViewModel = viewModel(factory = factory)
            val buscarViewModel: BuscarViewModel = viewModel(factory = factory)
            val ajustesViewModel: AjustesViewModel = viewModel(factory = factory)
            val loginViewModel: LoginViewModel = viewModel(factory = factory)
            val perfilViewModel: PerfilViewModel = viewModel(factory = factory)


            AppVideojuegosTheme(/*darkTheme = ajustesViewModel.cambiarModoOscuro*/) {

                Navigation(
                    principalViewModel = principalViewModel,
                    modificarViewModel = modificarViewModel,
                    insertarViewModel = insertarViewModel,
                    estadisticasViewModel = estadisticasViewModel,
                    detalleViewModel = detalleViewModel,
                    buscarViewModel = buscarViewModel,
                    ajustesViewModel = ajustesViewModel,
                    loginViewModel = loginViewModel,
                    perfilViewModel = perfilViewModel
                )
            }
        }
    }
}

