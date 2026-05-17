package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class ComunidadUiState(
    val listaVideojuegos: List<Videojuego> = emptyList(),
    val filtroEstado: String? = null,
    val soloFavoritos: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class ComunidadViewModel(private val repository: VideojuegoFirebaseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(ComunidadUiState(isLoading = true))
    val uiState: StateFlow<ComunidadUiState> = _uiState

    fun cambiarFiltroEstado(estado: String?) {
        _uiState.value = _uiState.value.copy(filtroEstado = estado)
        cargarVideojuegos()
    }

    fun cargarVideojuegos() {
        viewModelScope.launch {
            try {
                // Pasar el usuario guardado en la Sesion hacia Firebase
                repository.listarTotalComunidad().collect { lista ->
                    val filtrada = lista.filter { juego ->
                        val cumpleEstado = _uiState.value.filtroEstado == null || juego.estado == _uiState.value.filtroEstado
                        val cumpleFavoritos = !_uiState.value.soloFavoritos || juego.favorito
                        cumpleEstado && cumpleFavoritos
                    }
                    _uiState.value = _uiState.value.copy(
                        listaVideojuegos = filtrada,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                e.toString()
                _uiState.value = _uiState.value.copy(
                    listaVideojuegos = emptyList(),
                    isLoading = false,
                    error = "Error al cargar los videojuegos"
                )
            }
        }
    }
}