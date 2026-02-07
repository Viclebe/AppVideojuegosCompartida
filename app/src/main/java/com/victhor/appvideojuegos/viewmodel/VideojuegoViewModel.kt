package com.victhor.appvideojuegos.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.victhor.appvideojuegos.data.local.database.VideojuegoDatabase
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.domain.usecase.VideojuegoUseCase
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class VideojuegoViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = VideojuegoDatabase.obtenerInstancia(application).videojuegoDao()
    private val repositorio = VideojuegoRepository(dao)
    private val useCase = VideojuegoUseCase(repositorio)

    val listaVideojuegos = useCase.obtenerListaVideojuegos()

    fun insertar(videojuego: Videojuego) = viewModelScope.launch {
        useCase.insertarVideojuego(videojuego)
    }

    fun modificar(videojuego: Videojuego) = viewModelScope.launch {
        useCase.modificarVideojuego(videojuego)
    }

    fun eliminar(videojuego: Videojuego) = viewModelScope.launch {
        useCase.eliminarVideojuego(videojuego)
    }

    fun buscar(texto: String): LiveData<List<Videojuego>> {
        return if (texto.isBlank()) {
            listaVideojuegos
        } else
            useCase.buscarVideojuegos(texto)
    }

    fun buscarVideojuegoPorId(id: Int): LiveData<Videojuego> =
        useCase.buscarVideojuegoPorId(id)

    //Estadísticas
    val totalVideojuegos = useCase.totalVideojuegos()
    val jugando = useCase.totalVideojuegosJugando()
    val pendientes = useCase.totalVideojuegosPendiente()
    val finalizados = useCase.totalVideojuegosFinalizado()
    val mediaValoracion = useCase.mediaValoracion()
    val horasTotales = useCase.totalHoras()

    //Eliminar todo
    fun borrarBiblioteca() {
        useCase.eliminarBiblioteca()
    }

    //Modo Oscuro
    var modoOscuro by mutableStateOf(false)
        private set

    fun cambiarModoOscuro() {
        modoOscuro = !modoOscuro
    }
}