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

data class ModificarUiState(
    val titulo: String = "",
    val genero: String = "",
    val plataforma: String = "",
    val estado: String = "",
    val horasJugadas: String = "",
    val valoracion: String = "",
    val isLoading: Boolean = true,
    val errorHoras: Boolean = false,
    val errorValoracion: Boolean = false,
    val errorEstado: Boolean = false,
    val guardadoExitoso: Boolean = false
)

class ModificarViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VideojuegoDatabase.obtenerInstancia(application).videojuegoDao()
    private val repositorio = VideojuegoRepository(dao)

    private val _uiState = MutableStateFlow(ModificarUiState())
    val uiState: StateFlow<ModificarUiState> = _uiState

    fun cargarVideojuego(id: Int) {
        viewModelScope.launch {
            repositorio.buscarVideojuegoPorId(id).collect { juego ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    titulo = juego.titulo,
                    genero = juego.genero,
                    plataforma = juego.plataforma,
                    estado = juego.estado,
                    horasJugadas = juego.horasJugadas.toString(),
                    valoracion = juego.valoracion.toString()
                )
            }
        }
    }

    fun guardarCambiosTitulo(nuevoTitulo: String) {
        _uiState.value = _uiState.value.copy(titulo = nuevoTitulo)
    }

    fun guardarCambiosHorasJugadas(valor: String) {
        val error = valor.toIntOrNull()?.let { it < 0 } ?: false
        _uiState.value = _uiState.value.copy(
            horasJugadas = valor,
            errorHoras = error
        )
    }

    fun guardarCambiosGenero(nuevoGenero: String) {
        _uiState.value = _uiState.value.copy(genero = nuevoGenero)
    }

    fun guardarCambiosPlataforma(nuevaPlataforma: String) {
        _uiState.value = _uiState.value.copy(plataforma = nuevaPlataforma)
    }

    fun guardarCambiosEstado(nuevoEstado: String) {
        val estadosCorrectos = listOf("Jugando", "Pendiente", "Finalizado")
        val error = nuevoEstado.isNotBlank() && nuevoEstado !in estadosCorrectos

        _uiState.value = _uiState.value.copy(
            estado = nuevoEstado,
            errorEstado = error
        )
    }

    fun guardarCambiosValoracion(nuevaValoracion: String) {
        val error = nuevaValoracion.toDoubleOrNull()?.let {
            it < 0.0 || it > 5.0
        } ?: false

        _uiState.value = _uiState.value.copy(
            valoracion = nuevaValoracion,
            errorValoracion = error
        )
    }

    fun guardar(id: Int) {
        val state = _uiState.value

        if (state.errorHoras || state.errorValoracion || state.errorEstado) return

        viewModelScope.launch {
            repositorio.modificarVideojuego(
                Videojuego(
                    id = id,
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