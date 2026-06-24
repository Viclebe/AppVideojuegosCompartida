package com.victhor.appvideojuegos.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.domain.model.Usuario
import com.victhor.appvideojuegos.sesion.Sesion
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

//---UiState----
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = "",
    val registroExitoso: Boolean = false, // Indicarán a ViewModel que puede de navegar
    val loginExitoso: Boolean = false,
    val nombreUsuario: String = "",
    val mostrarDialogoNombre: Boolean = false
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
        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Crear usuario con Firebase Auth
                val authResult =
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                        .await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    val uidDeFirebase = firebaseUser.uid
                    // Guardar el usuario en Room para tener caché local (Opcional, pero respeta tu código actual)
                    val nuevoUsuario = Usuario(
                        uid = uidDeFirebase,
                        nombre = email.substringBefore("@"), // Nombre por defecto basado en email
                        fechaRegistro = System.currentTimeMillis(),
                        avatarUrl = null
                    )
                    repository.insertarUsuario(nuevoUsuario)

                    // Activamos registroExitoso en lugar de loginExitoso para no forzar la entrada directa
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        registroExitoso = false,
                        mostrarDialogoNombre = true,
                        error = null
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Fallo al registrar: ${e.localizedMessage}"
                )
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

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                // Iniciar sesión con Firebase Auth
                val authResult =
                    FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
                val firebaseUser = authResult.user

                if (firebaseUser != null) {
                    Sesion.usuarioId = firebaseUser.uid
                    _uiState.value =
                        _uiState.value.copy(isLoading = false, loginExitoso = true, error = null)
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Fallo al iniciar sesión: Correo o contraseña incorrectos"
                )
            }
        }
    }

    fun cambiarNombreUsuario(nombre: String) {
        _uiState.value = _uiState.value.copy(nombreUsuario = nombre)
    }

    fun guardarNombreYFinalizar(uid: String) {
        _uiState.value = _uiState.value.copy(isLoading = true)
        Sesion.usuarioId = uid

        viewModelScope.launch {
            val nuevoUsuario = Usuario(
                uid = uid,
                nombre = _uiState.value.nombreUsuario,
                email = _uiState.value.email,
                fechaRegistro = System.currentTimeMillis()
            )
            repository.insertarUsuario(nuevoUsuario)
            _uiState.value = _uiState.value.copy(
                isLoading = false,
                mostrarDialogoNombre = false,
                registroExitoso = true
            )
        }
    }
    fun limpiarEstado() {
        _uiState.value = LoginUiState()
    }

}