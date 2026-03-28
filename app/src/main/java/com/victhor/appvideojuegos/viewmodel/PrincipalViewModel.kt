package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class PrincipalUiState(
    val listaVideojuegos: List<Videojuego> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class PrincipalViewModel(private val repository: VideojuegoRepository) : ViewModel() {

    private val _uiState = MutableStateFlow(PrincipalUiState(isLoading = true))
    val uiState: StateFlow<PrincipalUiState> = _uiState

    init {
        cargarVideojuegos()
    }

    private fun cargarVideojuegos() {
        viewModelScope.launch {
            try {
                repository.listarVideojuegos().collect { lista ->
                    _uiState.value = PrincipalUiState(
                        listaVideojuegos = lista,
                        isLoading = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = PrincipalUiState(
                    listaVideojuegos = emptyList(),
                    isLoading = false,
                    error = "Error al cargar los videojuegos"
                )
            }
        }
    }
}