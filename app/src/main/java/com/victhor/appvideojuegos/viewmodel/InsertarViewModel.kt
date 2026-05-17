package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.data.repository.UsuarioVideojuegoRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.domain.model.Usuario
import com.victhor.appvideojuegos.domain.model.UsuarioVideojuego
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


// --- UI STATE ---
data class InsertarUiState(
    val titulo: String = "",
    val genero: String = "",
    val plataforma: String = "",
    val valoracion: String = "",
    val estado: String = "Pendiente",
    val horasJugadas: String = "0",
    val isLoading: Boolean = false,
    val errorValoracion: Boolean = false,
    val errorHoras: Boolean = false,
    val guardadoExitoso: Boolean = false,
    val nombreUsuario: String = "",
    val imagenUrl: String = ""
)

// --- VIEWMODEL ---
class InsertarViewModel(
    private val repository: VideojuegoRepository,
    private val usuarioVideojuegoRepository: UsuarioVideojuegoRepository,
    private val videojuegoFirebaseRepository: VideojuegoFirebaseRepository,
    private val reporitoryUsuario: UsuarioRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(InsertarUiState())
    val uiState: StateFlow<InsertarUiState> = _uiState

    // MODIFICAR
    fun cambiarTitulo(valor: String) {
        _uiState.value = _uiState.value.copy(titulo = valor)
    }

    fun cambiarGenero(valor: String) {
        _uiState.value = _uiState.value.copy(genero = valor)
    }

    fun cambiarPlataforma(valor: String) {
        _uiState.value = _uiState.value.copy(plataforma = valor)
    }

    fun cambiarValoracion(valor: String) {
        val error = valor.toDoubleOrNull()?.let { it < 0.0 || it > 5.0 } ?: false
        _uiState.value = _uiState.value.copy(
            valoracion = valor,
            errorValoracion = error
        )
    }

    fun cambiarEstado(nuevoEstado: String) {
        _uiState.value = _uiState.value.copy(estado = nuevoEstado)
    }

    fun cambiarImagenUrl(valor: String) {
        _uiState.value = _uiState.value.copy(imagenUrl = valor)
    }

    // Guardar los juegos nuevos
    fun guardar() {
        val state = _uiState.value
        if (state.errorValoracion) return

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            // Buscar el nombre real del repositorio usando firebase
            val emailActual = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.email ?: ""
            val usuarioActual = reporitoryUsuario.obtenerUsuarioPorEmail(emailActual)

            val nombreARegistrar = usuarioActual?.nombre ?: emailActual.substringBefore("@")
            // Crear objeto
            val nuevoJuego = Videojuego(
                id = 0,
                titulo = state.titulo,
                genero = state.genero,
                plataforma = state.plataforma,
                valoracion = state.valoracion.toDoubleOrNull() ?: 0.0,
                usuarioId = Sesion.usuarioId,
                nombreUsuario = nombreARegistrar,
                estado = state.estado,
                favorito = false,
                firestoreId = "",
                likes = emptyList(),
                fechaCreacionModificacion = System.currentTimeMillis(),
                imagenUrl = state.imagenUrl,
            )

            try {
                videojuegoFirebaseRepository.insertarVideojuego(nuevoJuego)
                _uiState.value = state.copy(guardadoExitoso = true)
            } catch (e: Exception) {
                e.toString()
            }
        }
    }

    /**
     * Reiniciar el estado de los videojuegos
     */
    fun reiniciarGuardadoExitoso(){
        _uiState.value= InsertarUiState()
    }
}