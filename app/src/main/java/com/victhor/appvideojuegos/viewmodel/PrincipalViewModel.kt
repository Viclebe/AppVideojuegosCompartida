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

data class PrincipalUiState(
    val listaVideojuegos: List<Videojuego> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


class PrincipalViewModel(application: Application) : AndroidViewModel(application) {

    private val dao = VideojuegoDatabase.obtenerInstancia(application).videojuegoDao()
    private val repositorio = VideojuegoRepository(dao)

    private val _uiState = MutableStateFlow(PrincipalUiState(isLoading = true))
    val uiState: StateFlow<PrincipalUiState> = _uiState

    init {
        viewModelScope.launch {
            repositorio.listarVideojuegos.collect { lista ->
                _uiState.value = PrincipalUiState(
                    listaVideojuegos = lista,
                    isLoading = false,
                    error = null
                )
            }
        }
    }
}