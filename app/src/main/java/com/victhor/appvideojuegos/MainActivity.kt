package com.victhor.appvideojuegos

import com.victhor.appvideojuegos.navigation.Navigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.victhor.appvideojuegos.ui.theme.AppVideojuegosTheme
import com.victhor.appvideojuegos.viewmodel.AjustesViewModel
import com.victhor.appvideojuegos.viewmodel.BuscarViewModel
import com.victhor.appvideojuegos.viewmodel.DetalleViewModel
import com.victhor.appvideojuegos.viewmodel.EstadisticasViewModel
import com.victhor.appvideojuegos.viewmodel.InsertarViewModel
import com.victhor.appvideojuegos.viewmodel.ModificarViewModel
import com.victhor.appvideojuegos.viewmodel.PrincipalViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val principalViewModel: PrincipalViewModel = viewModel()
            val modificarViewModel: ModificarViewModel = viewModel()
            val insertarViewModel: InsertarViewModel = viewModel()
            val estadisticasViewModel: EstadisticasViewModel = viewModel()
            val detalleViewModel: DetalleViewModel = viewModel()
            val buscarViewModel: BuscarViewModel = viewModel()
            val ajustesViewModel: AjustesViewModel = viewModel()


            AppVideojuegosTheme(/*darkTheme = ajustesViewModel.cambiarModoOscuro*/) {

                Navigation(
                    principalViewModel = principalViewModel,
                    modificarViewModel = modificarViewModel,
                    insertarViewModel=insertarViewModel,
                    estadisticasViewModel=estadisticasViewModel,
                    detalleViewModel=detalleViewModel,
                    buscarViewModel=buscarViewModel,
                    ajustesViewModel=ajustesViewModel
                )
            }
        }
    }
}

