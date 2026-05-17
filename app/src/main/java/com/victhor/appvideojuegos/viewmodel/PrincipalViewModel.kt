package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PrincipalUiState(
    val listaVideojuegos: List<Videojuego> = emptyList(),
    val filtroEstado: String? = null,
    val soloFavoritos: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class PrincipalViewModel(private val repository: VideojuegoFirebaseRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PrincipalUiState(isLoading = true))
    val uiState: StateFlow<PrincipalUiState> = _uiState

    fun cambiarFiltroEstado(estado: String?) {
        _uiState.value = _uiState.value.copy(filtroEstado = estado)
        cargarVideojuegos()
    }

    fun alternarFavoritos() {
        _uiState.value = _uiState.value.copy(soloFavoritos = !_uiState.value.soloFavoritos)
        cargarVideojuegos()
    }

    fun cargarVideojuegos() {
    viewModelScope.launch {
            try {
                repository.listarVideojuegos(Sesion.usuarioId).collect { lista ->
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
                _uiState.value = _uiState.value.copy(
                    listaVideojuegos = emptyList(),
                    isLoading = false,
                    error = "Error al cargar los videojuegos"
                )
            }
        }
    }
}