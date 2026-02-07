package com.victhor.appvideojuegos

import com.victhor.appvideojuegos.navigation.Navigation
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.victhor.appvideojuegos.ui.theme.AppVideojuegosTheme
import com.victhor.appvideojuegos.viewmodel.VideojuegoViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: VideojuegoViewModel=viewModel()
            val modoOscuro = viewModel.modoOscuro

            AppVideojuegosTheme(darkTheme = modoOscuro){
                //val viewModel: VideojuegoViewModel = viewModel()
                Navigation(videojuegoViewModel = viewModel)
            }
        }
    }
}

