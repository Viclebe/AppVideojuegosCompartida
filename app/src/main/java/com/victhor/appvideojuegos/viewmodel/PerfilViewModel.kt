package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.domain.model.Usuario
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//--------UiState--------------
data class PerfilUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val perfilReconocido: Boolean = false
)

//---------VieewModel------------
class PerfilViewModel(private val repository: UsuarioRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState

    //*********Eventos de los campos de texto en pantalla************
    /**
     * Obtener usuario desde repository usando Sesion.usuarioId
     */
    fun cargarPerfil() {
        val uid = Sesion.usuarioId

        if (uid.isBlank()) {
            return
        }

        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            repository.obtenerUsuario(uid).collect { usuario ->
                if (usuario != null) {
                    _uiState.value = _uiState.value.copy(
                        email = usuario.email,
                        password = "", 
                        isLoading = false,
                        perfilReconocido = true
                    )
                } else {
                    _uiState.value = _uiState.value.copy(isLoading = false, perfilReconocido = false)
                }
            }
        }

    }

    fun cerrarSesion() {
        Sesion.usuarioId = ""
    }
}