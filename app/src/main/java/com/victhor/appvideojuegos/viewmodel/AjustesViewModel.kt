package com.victhor.appvideojuegos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.local.database.VideojuegoDatabase
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AjustesUiState(
    val modoOscuro: Boolean = false,
    val mostrarDialogoBorrar: Boolean = false
)

    class AjustesViewModel(application: Application) : AndroidViewModel(application) {

        private val dao = VideojuegoDatabase.obtenerInstancia(application).videojuegoDao()
        private val repositorio = VideojuegoRepository(dao)

        private val _uiState = MutableStateFlow(AjustesUiState())
        val uiState: StateFlow<AjustesUiState> = _uiState

        // Cambiar modo oscuro
        fun cambiarModoOscuro() {
            _uiState.value = _uiState.value.copy(modoOscuro = !_uiState.value.modoOscuro)
        }

        // Mostrar diálogo de confirmación
        fun mostrarDialogoBorrar() {
            _uiState.value = _uiState.value.copy(mostrarDialogoBorrar = true)
        }

        fun ocultarDialogoBorrar() {
            _uiState.value = _uiState.value.copy(mostrarDialogoBorrar = false)
        }

        // Borrar biblioteca
        fun borrarBiblioteca() {
            viewModelScope.launch {
                repositorio.eliminarTodo()
                ocultarDialogoBorrar()
            }
        }
    }
