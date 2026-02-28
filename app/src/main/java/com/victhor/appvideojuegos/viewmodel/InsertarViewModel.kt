package com.victhor.appvideojuegos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.local.database.VideojuegoDatabase
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// --- UI STATE ---
data class InsertarUiState(
    val titulo: String = "",
    val genero: String = "",
    val plataforma: String = "",
    val estado: String = "",
    val horasJugadas: String = "",
    val valoracion: String = "",
    val isLoading: Boolean = false,
    val errorHoras: Boolean = false,
    val errorValoracion: Boolean = false,
    val errorEstado: Boolean = false,
    val guardadoExitoso: Boolean = false
)

// --- VIEWMODEL ---
class InsertarViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VideojuegoDatabase.obtenerInstancia(application).videojuegoDao()
    private val repositorio = VideojuegoRepository(dao)

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

    fun cambiarEstado(valor: String) {
        val estadosValidos = listOf("Jugando", "Pendiente", "Finalizado")
        val error = valor.isNotBlank() && valor !in estadosValidos
        _uiState.value = _uiState.value.copy(
            estado = valor,
            errorEstado = error
        )
    }

    fun cambiarHorasJugadas(valor: String) {
        val error = valor.toIntOrNull()?.let { it < 0 } ?: false
        _uiState.value = _uiState.value.copy(
            horasJugadas = valor,
            errorHoras = error
        )
    }

    fun cambiarValoracion(valor: String) {
        val error = valor.toDoubleOrNull()?.let { it < 0.0 || it > 5.0 } ?: false
        _uiState.value = _uiState.value.copy(
            valoracion = valor,
            errorValoracion = error
        )
    }

    // --- GUARDAR NUEVO VIDEOJUEGO ---
    fun guardar() {
        val state = _uiState.value

        if (state.errorHoras || state.errorValoracion || state.errorEstado) return

        viewModelScope.launch {
            repositorio.insertarVideojuego(
                Videojuego(
                    id = 0, // 0 para que Room genere id automáticamente
                    titulo = state.titulo,
                    genero = state.genero,
                    plataforma = state.plataforma,
                    estado = state.estado,
                    horasJugadas = state.horasJugadas.toIntOrNull() ?: 0,
                    valoracion = state.valoracion.toDoubleOrNull() ?: 0.0
                )
            )

            _uiState.value = state.copy(guardadoExitoso = true)
        }
    }
}