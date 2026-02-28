package com.victhor.appvideojuegos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.local.database.VideojuegoDatabase
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
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
class EstadisticasViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VideojuegoDatabase.obtenerInstancia(application).videojuegoDao()
    private val repositorio = VideojuegoRepository(dao)

    private val _uiState = MutableStateFlow(EstadisticasUiState())
    val uiState: StateFlow<EstadisticasUiState> = _uiState

    init {
        cargarEstadisticas()
    }

    private fun cargarEstadisticas() {
        viewModelScope.launch {
            // Suscribirse a todos los flujos y actualizar el uiState
            repositorio.contarVideojuegos().collect { total ->
                _uiState.value = _uiState.value.copy(total = total)
            }
        }

        viewModelScope.launch {
            repositorio.contarPorEstado("Jugando").collect { jugando ->
                _uiState.value = _uiState.value.copy(jugando = jugando)
            }
        }

        viewModelScope.launch {
            repositorio.contarPorEstado("Pendiente").collect { pendientes ->
                _uiState.value = _uiState.value.copy(pendientes = pendientes)
            }
        }

        viewModelScope.launch {
            repositorio.contarPorEstado("Finalizado").collect { finalizados ->
                _uiState.value = _uiState.value.copy(finalizados = finalizados)
            }
        }

        viewModelScope.launch {
            repositorio.mediaValoracion().collect { media ->
                _uiState.value = _uiState.value.copy(mediaValoracion = media)
            }
        }

        viewModelScope.launch {
            repositorio.contarHorasJugadas().collect { horas ->
                _uiState.value = _uiState.value.copy(horasTotales = horas, isLoading = false)
            }
        }
    }
}