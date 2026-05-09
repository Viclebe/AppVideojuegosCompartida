package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.data.repository.UsuarioVideojuegoRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.domain.model.UsuarioVideojuego
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// --- UI STATE ---
data class InsertarUiState(
    val titulo: String = "",
    val genero: String = "",
    val plataforma: String = "",
    val valoracion: String = "",
    val estado: String = "Pendiente",
    val horasJugadas: String = "0",
    val isLoading: Boolean = false,
    val errorValoracion: Boolean = false,
    val errorHoras: Boolean = false,
    val guardadoExitoso: Boolean = false
)

// --- VIEWMODEL ---
class InsertarViewModel(
    private val repository: VideojuegoRepository,
    private val usuarioVideojuegoRepository: UsuarioVideojuegoRepository,
    private val videojuegoFirebaseRepository: VideojuegoFirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsertarUiState())
    val uiState: StateFlow<InsertarUiState> = _uiState

    // --- FUNCIONES DE CAMBIO ---
    fun cambiarTitulo(valor: String) {
        _uiState.value = _uiState.value.copy(titulo = valor)
    }

    fun cambiarGenero(valor: String) {
        _uiState.value = _uiState.value.copy(genero = valor)
    }

    fun cambiarPlataforma(valor: String) {
        _uiState.value = _uiState.value.copy(plataforma = valor)
    }

    fun cambiarValoracion(valor: String) {
        val error = valor.toDoubleOrNull()?.let { it < 0.0 || it > 5.0 } ?: false
        _uiState.value = _uiState.value.copy(
            valoracion = valor,
            errorValoracion = error
        )
    }

    fun cambiarEstado(nuevoEstado: String) {
        _uiState.value = _uiState.value.copy(estado = nuevoEstado)
    }

    fun cambiarHoras(valor: String) {
        // PERMITIMOS vacío (será 0) o números positivos. No bloqueamos si está vacío.
        val error = valor.isNotEmpty() && (valor.toIntOrNull() == null || valor.toInt() < 0)
        _uiState.value = _uiState.value.copy(
            horasJugadas = valor,
            errorHoras = error
        )
    }

    // --- GUARDAR NUEVO VIDEOJUEGO ---
    fun guardar() {
        val state = _uiState.value

        if (state.errorValoracion || state.errorHoras) return

        viewModelScope.launch {
            // Creamos el objeto completo para Firebase
            val nuevoJuego = Videojuego(
                id = 0,
                titulo = state.titulo,
                genero = state.genero,
                plataforma = state.plataforma,
                valoracion = state.valoracion.toDoubleOrNull() ?: 0.0,
                usuarioId = Sesion.usuarioId,
                estado = state.estado,
                favorito = false,
                firestoreId = "",
                likes = emptyList(),
                fechaCreacionModificacion = System.currentTimeMillis()
            )

            // Guardamos directamente en Firebase
            try {
                videojuegoFirebaseRepository.insertarVideojuego(nuevoJuego)
                _uiState.value = state.copy(guardadoExitoso = true)
            } catch (e: Exception) {
                // Si hubiera un error de red, aquí podrías manejarlo
            }
        }
    }

    /**
     * Reiniciar el estado de los videojuegos
     */
    fun reiniciarGuardadoExitoso(){
        _uiState.value= InsertarUiState()
    }
}