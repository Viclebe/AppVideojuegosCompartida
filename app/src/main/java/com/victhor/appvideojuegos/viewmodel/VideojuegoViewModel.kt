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

    fun buscarVideojuegoPorId(id: Int): LiveData<Videojuego> =
        useCase.buscarVideojuegoPorId(id)
}