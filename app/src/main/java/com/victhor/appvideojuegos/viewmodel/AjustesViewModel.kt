package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class AjustesUiState(
    val modoOscuro: Boolean = false,
    val mostrarDialogoBorrar: Boolean = false,
)

//Ahora contruimos con el repositorio de firebase
class AjustesViewModel(private val repository: VideojuegoFirebaseRepository) : ViewModel() {

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

    // Borrar biblioteca. Ahora la Función está en FirebaseRepository
    fun borrarBiblioteca() {
        viewModelScope.launch {
            repository.eliminarTodaBiblioteca(Sesion.usuarioId)
            ocultarDialogoBorrar()
        }
    }
}
