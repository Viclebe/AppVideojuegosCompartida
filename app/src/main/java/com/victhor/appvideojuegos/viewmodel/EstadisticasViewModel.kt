package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.data.repository.UsuarioVideojuegoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// --- UI STATE ---
data class EstadisticasUiState(
    val total: Int = 0,
    val jugando: Int = 0,
    val pendientes: Int = 0,
    val finalizados: Int = 0,
    val mediaValoracion: Double = 0.0,
    val horasTotales: Int = 0,
    val isLoading: Boolean = true
)

// --- VIEWMODEL ---
class EstadisticasViewModel(
    private val repository: VideojuegoRepository,
    private val usuarioVideojuegoRepository: UsuarioVideojuegoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstadisticasUiState())
    val uiState: StateFlow<EstadisticasUiState> = _uiState

    init {
        cargarEstadisticas()
    }

    private fun cargarEstadisticas() {
        viewModelScope.launch {
            // Suscribirse a todos los flujos y actualizar el uiState
            repository.contarVideojuegos().collect { total ->
                _uiState.value = _uiState.value.copy(total = total)
            }
        }

        viewModelScope.launch {
            repository.mediaValoracion().collect { media ->
                _uiState.value = _uiState.value.copy(mediaValoracion = media ?: 0.0, isLoading = false)
            }
        }


        viewModelScope.launch {
            usuarioVideojuegoRepository.contarPorEstado("Jugando").collect { jugando ->
                _uiState.value = _uiState.value.copy(jugando = jugando)
            }
        }

        viewModelScope.launch {
            usuarioVideojuegoRepository.contarPorEstado("Pendiente").collect { pendientes ->
                _uiState.value = _uiState.value.copy(pendientes = pendientes)
            }
        }

        viewModelScope.launch {
            usuarioVideojuegoRepository.contarPorEstado("Finalizado").collect { finalizados ->
                _uiState.value = _uiState.value.copy(finalizados = finalizados)
            }
        }

        viewModelScope.launch {
            usuarioVideojuegoRepository.contarHorasJugadas().collect { horas ->
                _uiState.value = _uiState.value.copy(horasTotales = horas, isLoading = false)
            }
        }

    }
}