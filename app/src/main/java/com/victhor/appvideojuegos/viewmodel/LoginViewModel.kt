package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.domain.model.Usuario
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

//---UiState----
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = "",
    val registroExitoso: Boolean = false, // Indicarán a ViewModel que puede de navegar
    val loginExitoso: Boolean = false
)

//---ViewModel-----
class LoginViewModel(private val repository: UsuarioRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState

    //******Eventos de los campos de tenxto en pantalla********
    fun cambiarEmail(email: String) {
        _uiState.value = _uiState.value.copy(email = email, error = null)
    }

    fun cambiarPassword(password: String) {
        _uiState.value = _uiState.value.copy(password = password, error = null)
    }


    /**
     * Registrarse por primera vez (simulado). Validar que los campos no estén vacíos o sean incorrectos.
     * Obtiene los datos email y password del estado UI. Lanzar la corrutina viewModelScope.launch y consultar al repositorio.
     *
     * @param email
     * @param password
     */
    fun registrarUsuario() {
        val email = _uiState.value.email
        val password = _uiState.value.password

        // Comprobar que no son null
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Rellene los dos campos, por favor.")
            return // Termina
        }

        //Lanzar corrutina
        _uiState.value = _uiState.value.copy(isLoading = true)

        // Registrar un usuario simulado, inventamos la uid (Firebase la crea automáticamente después)
        viewModelScope.launch {
            val uidDeMentiras = "uid123456"

            try {
                val nuevoUsuario = Usuario( //Insertar en Room sin guardar el password
                    uid = uidDeMentiras,
                    nombre = "Jugador1",
                    fechaRegistro = System.currentTimeMillis(),
                    avatarUrl = null
                )
                repository.insertarUsuario(nuevoUsuario)
                Sesion.usuarioId = uidDeMentiras// Logear usuario
                _uiState.value = _uiState.value.copy(isLoading = false, loginExitoso = true, error = null)

            } catch (e: Exception) {
                //Excepción de Room si el correo ya existe (UsuarioEntity email es unique = true)
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Este correo ya está registrado.")
            }
        }
    }

    /**
     * Iniciar sesión buscando al usuario por su email. Validar que los campos no estén vacíos o sean incorrectos.
     * Obtiene los datos email y password del estado UI. Lanzar la corrutina viewModelScope.launch y consultar al repositorio.
     *
     * @param email
     * @param password
     */
    fun iniciarSesion() {
        val email = _uiState.value.email
        val password = _uiState.value.password


        // Comprobar que no son null
        if (email.isBlank() || password.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "Rellene los dos campos, por favor.")
            return // Termina
        }

        // Buscar si en la BBDD existe un usuario con ese email, lanzar corrutina
        _uiState.value = _uiState.value.copy(isLoading = true)

        viewModelScope.launch {
            val usuarioEncontrado = repository.obtenerUsuarioPorEmail(email)

            if (usuarioEncontrado != null) {
                // Es simulado, todavía no se puede validar pass en Room, guardamos el uid en sesión global, toda la app lo sabe
                Sesion.usuarioId = usuarioEncontrado.uid
                // Y de PantallaRegistro ya puede navegar a PantallaPrincipal
                _uiState.value =
                    _uiState.value.copy(isLoading = false, loginExitoso = true, error = null)

            } else {
                //Usuario no encontrado
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "El usuario no existe. Regístrate primero."
                )
            }
        }
    }
}