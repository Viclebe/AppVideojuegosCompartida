package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// --- UI STATE ---
data class DetalleUiState(
    val videojuego: Videojuego? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

// --- VIEWMODEL ---
class DetalleViewModel(private val repository: VideojuegoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState

    fun cargarVideojuego(id: Int) {
        viewModelScope.launch {
            try {
                repository.buscarVideojuegoPorId(id).collect { juego ->
                    _uiState.value = DetalleUiState(
                        videojuego = juego,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _uiState.value = DetalleUiState(
                    videojuego = null,
                    isLoading = false,
                    error = "Error al cargar el videojuego"
                )
            }
        }
    }

    fun eliminarVideojuego() {
        val juego = _uiState.value.videojuego ?: return
        viewModelScope.launch {
            repository.eliminarVideojuego(juego)
            _uiState.value = DetalleUiState(videojuego = null, isLoading = false)
        }
    }
}