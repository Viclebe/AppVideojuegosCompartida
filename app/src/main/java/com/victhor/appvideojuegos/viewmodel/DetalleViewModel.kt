package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.data.repository.UsuarioVideojuegoRepository
import com.victhor.appvideojuegos.data.repository.ComentarioRepository
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.data.repository.ValoracionRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.domain.model.Comentario
import com.victhor.appvideojuegos.domain.model.UsuarioVideojuego
import com.victhor.appvideojuegos.domain.model.Valoracion
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// --- UI STATE ---
data class DetalleUiState(
    val videojuego: Videojuego? = null,
    val isLoading: Boolean = true,
    val error: String? = null,
    val comentarios: List<Comentario> = emptyList(),
    val nombreUsuario: String = "",
    val mediaValoracion: Double = 0.0,
    val votosTotales: Int = 0,
    val isFavorito: Boolean = false,
    val estadoPersonal: String = ""
)

// --- VIEWMODEL ---
class DetalleViewModel(
    private val repository: VideojuegoRepository,
    private val comentarioRepository: ComentarioRepository,
    private val usuarioRepository: UsuarioRepository,
    private val valoracionRepository: ValoracionRepository,
    private val usuarioVideojuegoRepository: UsuarioVideojuegoRepository,
    private val videojuegoFirebaseRepository: VideojuegoFirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetalleUiState())
    val uiState: StateFlow<DetalleUiState> = _uiState

    fun cargarVideojuego(idFirestore: String) {
        viewModelScope.launch {
            try {
                // Hilo para cargar el videojuego desde FIREBASE
                launch {
                    try {
                        videojuegoFirebaseRepository.buscarPorId(idFirestore).collect { juego ->
                            _uiState.value = _uiState.value.copy(
                                videojuego = juego,
                                isLoading = false
                            )
                        }
                    } catch (e: Exception) {
                        // Ignorar fallo puntual
                        _uiState.value = _uiState.value.copy(isLoading = false, error = "Error red")
                    }
                }

                // hilo para cargar los comentarios asociados a este videojuego
                launch {
                    try {
                        videojuegoFirebaseRepository.obtenerComentarios(idFirestore).collect { lista ->
                            _uiState.value = _uiState.value.copy(
                                comentarios = lista
                            )
                        }
                    } catch (e: Exception) { }
                }

                // Hilo para obtener el nombre del usuario actual (en sesión)
                launch {
                    try {
                        val uidActual = Sesion.usuarioId
                        if (uidActual.isNotEmpty()) {
                            usuarioRepository.obtenerUsuario(uidActual).collect { usuario ->
                                _uiState.value = _uiState.value.copy(
                                    nombreUsuario = usuario?.nombre ?: ""
                                )
                            }
                        }
                    } catch (e: Exception) { }
                }

                // Hilo para obtener la media de valoración del videojuego
                // Ahora los likes llaman a la función darLike() de VideojuegoFirebaseRepository
                /*launch {
                    valoracionRepository.calcularMediaGlobal(id).collect { double ->
                        _uiState.value = _uiState.value.copy(
                            mediaValoracion = double
                        )
                    }
                }
                */

                // Hilo para obtener los votos totales
                launch {
                    // FIXME: De momento usamos 0 hasta migrar valoraciones a Firebase
                    valoracionRepository.contarVotos(0).collect { int ->
                        _uiState.value = _uiState.value.copy(
                            votosTotales = int
                        )
                    }
                }

                // Cargar estado personal
                launch {
                    // FIXME: De momento usamos 0 hasta migrar progreso a Firebase
                    usuarioVideojuegoRepository.obtenerProgreso(0).collect { progreso ->
                        // Si progreso no nulo, actualizar estado
                        _uiState.value = _uiState.value.copy(
                            isFavorito = progreso?.favorito ?: false,
                            estadoPersonal = progreso?.estado ?: "Sin estado"
                        )
                    }
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Error al cargar los datos en detalle"
                )
            }
        }
    }
    fun pulsarLike(idFirestore: String) {
        viewModelScope.launch{
            videojuegoFirebaseRepository.darLike(idFirestore, Sesion.usuarioId)
        }
    }


    fun enviarComentario(texto: String, firestoreIdVideojuego: String) {
        if (texto.isBlank()) return
        viewModelScope.launch {
            val nuevoComentario = Comentario(
                texto = texto,
                fechaComentario = System.currentTimeMillis(),
                usuarioId = Sesion.usuarioId,
                nombreUsuario = _uiState.value.nombreUsuario, // Sacamos el nombre del usuario actual del estado
                firestoreIdVideojuego = firestoreIdVideojuego
            )
            videojuegoFirebaseRepository.guardarComentario(nuevoComentario)
        }
    }

    fun enviarValoracion(estrellas: Int, idVideojuego: Int) {
        viewModelScope.launch {
            val nuevaValoracion = Valoracion(
                usuarioId = Sesion.usuarioId,
                videojuegoId = idVideojuego,
                puntuacion = estrellas
            )
            valoracionRepository.insertarValoracion(nuevaValoracion)
        }
    }

    fun eliminarVideojuego() {
        val juego = _uiState.value.videojuego ?: return
        viewModelScope.launch {
            repository.eliminarVideojuego(juego)
            _uiState.value = DetalleUiState(videojuego = null, isLoading = false)
        }
    }

    /**
     * Alternar favorito (corazón)
     */
    fun toggleFavorito() {
        val juegoId = _uiState.value.videojuego?.id ?: return
        val nuevoFavorito = _uiState.value.isFavorito

        viewModelScope.launch {
            val nuevoProgreso = UsuarioVideojuego(
                videojuegoId = juegoId,
                favorito = !nuevoFavorito,
                estado = _uiState.value.estadoPersonal
            )
            usuarioVideojuegoRepository.guardarProgreso(nuevoProgreso)
        }
    }

    /**
     * Cambiar el estado.
     */
    fun cambiarEstado(nuevoEstado: String) {
        val juegoId = _uiState.value.videojuego?.id ?: return

        viewModelScope.launch {
            val nuevoProgreso = UsuarioVideojuego(
                videojuegoId = juegoId,
                favorito = _uiState.value.isFavorito,
                estado = nuevoEstado
            )
            usuarioVideojuegoRepository.guardarProgreso(nuevoProgreso)
        }
    }
}