package com.victhor.appvideojuegos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.local.database.VideojuegoDatabase
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class VideojuegoViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = VideojuegoDatabase.obtenerInstancia(application).videojuegoDao()
    private val repositorio = VideojuegoRepository(dao)

    val listaVideojuegos: StateFlow<List<Videojuego>> =
        repositorio.listarVideojuegos
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = emptyList()
            )

    fun insertar(videojuego: Videojuego) = viewModelScope.launch {
        repositorio.insertarVideojuego(videojuego)
    }

    fun modificar(videojuego: Videojuego) = viewModelScope.launch {
        repositorio.modificarVideojuego(videojuego)
    }

    fun eliminar(videojuego: Videojuego) = viewModelScope.launch {
        repositorio.eliminarVideojuego(videojuego)
    }

    fun buscar(texto: String): StateFlow<List<Videojuego>> {
        return if (texto.isBlank()) {
            listaVideojuegos // Ya es StateFlow
        } else {
            repositorio.buscarVideojuego(texto)
                .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
        }
    }

    fun buscarVideojuegoPorId(id: Int): StateFlow<Videojuego> {
        return repositorio.buscarVideojuegoPorId(id)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Lazily,
                initialValue = Videojuego(0, "", "", "", "", 0, 0.0)
            )
    }

    // Estadísticas
    val totalVideojuegos: StateFlow<Int> =
        repositorio.contarVideojuegos().stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val jugando: StateFlow<Int> =
        repositorio.contarPorEstado("Jugando").stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val pendientes: StateFlow<Int> =
        repositorio.contarPorEstado("Pendiente").stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val finalizados: StateFlow<Int> =
        repositorio.contarPorEstado("Finalizado").stateIn(viewModelScope, SharingStarted.Lazily, 0)

    val mediaValoracion: StateFlow<Double> =
        repositorio.mediaValoracion().stateIn(viewModelScope, SharingStarted.Lazily, 0.0)

    val horasTotales: StateFlow<Int> =
        repositorio.contarHorasJugadas().stateIn(viewModelScope, SharingStarted.Lazily, 0)

    // Borrar toda la biblioteca
    fun borrarBiblioteca() = repositorio.eliminarTodo()

    //Modo Oscuro
    var modoOscuro by mutableStateOf(false)
        private set

    fun cambiarModoOscuro() {
        modoOscuro = !modoOscuro
    }
}