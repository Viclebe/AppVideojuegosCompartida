package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.data.repository.UsuarioVideojuegoRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.domain.model.UsuarioVideojuego
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.victhor.appvideojuegos.sesion.Sesion


data class ModificarUiState(
    val titulo: String = "",
    val genero: String = "",
    val plataforma: String = "",
    val valoracion: String = "",
    val estado: String = "Pendiente",
    val horasJugadas: String = "0",
    val isFavorito: Boolean = false,
    val isLoading: Boolean = true,
    val errorValoracion: Boolean = false,
    val errorHoras: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val idDeFirebase: String = "",
    val nombreUsuario: String = "",
    val imagenUrl: String = ""
)

class ModificarViewModel(
    private val videojuegoFirebaseRepository: VideojuegoFirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ModificarUiState())
    val uiState: StateFlow<ModificarUiState> = _uiState

    fun cargarVideojuego(idFirestore: String) {
        viewModelScope.launch {
            videojuegoFirebaseRepository.buscarPorId(idFirestore).collect { juego ->
                juego?.let {
                    _uiState.value = _uiState.value.copy(
                        titulo = it.titulo,
                        genero = it.genero,
                        plataforma = it.plataforma,
                        valoracion = it.valoracion.toString(),
                        estado = it.estado ?: "Pendiente",
                        isFavorito = it.favorito,
                        idDeFirebase = it.firestoreId,
                        nombreUsuario = it.nombreUsuario,
                        imagenUrl = it.imagenUrl,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun guardarCambiosTitulo(nuevoTitulo: String) {
        _uiState.value = _uiState.value.copy(titulo = nuevoTitulo)
    }

    fun guardarCambiosGenero(nuevoGenero: String) {
        _uiState.value = _uiState.value.copy(genero = nuevoGenero)
    }

    fun guardarCambiosPlataforma(nuevaPlataforma: String) {
        _uiState.value = _uiState.value.copy(plataforma = nuevaPlataforma)
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

    fun guardarCambiosEstado(nuevoEstado: String) {
        _uiState.value = _uiState.value.copy(estado = nuevoEstado)
    }

    fun cambiarImagenUrl(valor: String) {
        _uiState.value = _uiState.value.copy(imagenUrl = valor)
    }
    /*
    fun guardarCambiosHoras(valor: String) {
        val error = valor.toIntOrNull() == null || valor.toInt() < 0
        _uiState.value = _uiState.value.copy(
            horasJugadas = valor,
            errorHoras = error
        )
    }
    */
    fun guardar() { 
        val state = _uiState.value

        if (state.errorValoracion || state.errorHoras) return

        viewModelScope.launch {
            val juegoEditado = Videojuego(
                id = 0,
                titulo = state.titulo,
                genero = state.genero,
                plataforma = state.plataforma,
                valoracion = state.valoracion.toDoubleOrNull() ?: 0.0,
                usuarioId = Sesion.usuarioId,
                nombreUsuario = state.nombreUsuario,
                estado = state.estado,
                favorito = state.isFavorito,
                firestoreId = state.idDeFirebase,
                imagenUrl = state.imagenUrl
            )

            videojuegoFirebaseRepository.modificarVideojuego(state.idDeFirebase, juegoEditado)

            _uiState.value = state.copy(guardadoExitoso = true)
        }
    }
}