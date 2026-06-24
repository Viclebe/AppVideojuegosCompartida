package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.domain.model.Usuario
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

//--------UiState--------------
data class PerfilUiState(
    val email: String = "",
    val password: String = "",
    val nombreUsuario:String="",
    val isLoading: Boolean = false,
    val perfilReconocido: Boolean = false,
    val avatarUrl: String? = null
)

//---------VieewModel------------
class PerfilViewModel(private val repository: UsuarioRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(PerfilUiState())
    val uiState: StateFlow<PerfilUiState> = _uiState

    fun cargarPerfil() {
        viewModelScope.launch {
            repository.obtenerUsuario(Sesion.usuarioId).collect { usuario ->
                usuario?.let {
                    _uiState.value = _uiState.value.copy(
                        nombreUsuario = it.nombre,
                        email = it.email,
                        avatarUrl = it.avatarUrl,
                        perfilReconocido = true
                    )
                }
            }
        }
    }

    fun actualizarAvatar(nuevaUrl: String) {
        viewModelScope.launch {
            // Avatar por usuario actual
            val usuario = repository.obtenerUsuario(Sesion.usuarioId).first()
            usuario?.let {
                val usuarioActualizado = it.copy(avatarUrl = nuevaUrl)
                repository.insertarUsuario(usuarioActualizado)
            }
        }
    }

    fun cerrarSesion() {
        Sesion.usuarioId = ""
        com.google.firebase.auth.FirebaseAuth.getInstance().signOut()    }
}