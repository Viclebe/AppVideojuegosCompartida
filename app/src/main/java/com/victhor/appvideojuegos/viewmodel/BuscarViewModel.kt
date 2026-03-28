package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

data class BuscarUiState(
    val textoBusqueda: String = "",
    val resultados: List<Videojuego> = emptyList(),
    val isLoading: Boolean = false
)

class BuscarViewModel(private val repository: VideojuegoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(BuscarUiState())
    val uiState: StateFlow<BuscarUiState> = _uiState

    fun cambiarTexto(texto: String) {
        _uiState.value = _uiState.value.copy(
            textoBusqueda = texto,
            isLoading = true
        )

        viewModelScope.launch {
            if (texto.isBlank()) {
                repository.listarVideojuegos().collect { lista ->
                    _uiState.value = _uiState.value.copy(
                        resultados = lista,
                        isLoading = false
                    )
                }
            } else {
                repository.buscarVideojuego(texto).collectLatest { lista ->
                    _uiState.value = _uiState.value.copy(
                        resultados = lista,
                        isLoading = false
                    )
                }
            }
        }
    }
}