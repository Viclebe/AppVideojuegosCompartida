package com.victhor.appvideojuegos.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class EstadisticasUiState(
    val total: Int = 0,
    val jugando: Int = 0,
    val pendientes: Int = 0,
    val finalizados: Int = 0,
    val totalFavoritos: Int = 0,
    val mediaValoracion: Double = 0.0,
    val juegosPorPlataforma: Map<String, Int> = emptyMap(),
    val isLoading: Boolean = false
)

class EstadisticasViewModel(
    private val firebaseRepository: VideojuegoFirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EstadisticasUiState())
    val uiState: StateFlow<EstadisticasUiState> = _uiState

    init {
        cargarEstadisticas()
    }

    fun cargarEstadisticas() {
        val uid = Sesion.usuarioId
        if (uid.isEmpty()) {
            _uiState.update { EstadisticasUiState(isLoading = false) }
            return
        }

        _uiState.update { it.copy(isLoading = true) }

        viewModelScope.launch {
            firebaseRepository.listarVideojuegos(uid).collect { lista ->
                //Log.d("TRACestadísticas: Juegos recibidos de Firebase: ${lista.size}")
                _uiState.update { state ->
                    state.copy(
                        total = lista.size,
                        totalFavoritos = lista.count { it.favorito },
                        jugando = lista.count { it.estado.equals("Jugando", true) },
                        finalizados = lista.count { it.estado.equals("Finalizado", true) },
                        pendientes = lista.count {
                            it.estado.equals(
                                "Pendiente",
                                true
                            ) || it.estado.isEmpty()
                        },
                        mediaValoracion = if (lista.isNotEmpty()) {
                            String.format("%.2f", lista.map { it.valoracion }.average()).toDouble()
                        } else {
                            0.0
                        },
                        juegosPorPlataforma = lista.groupBy { it.plataforma }
                            .mapValues { it.value.size },
                        isLoading = false
                    )
                }
            }
        }
    }
}
