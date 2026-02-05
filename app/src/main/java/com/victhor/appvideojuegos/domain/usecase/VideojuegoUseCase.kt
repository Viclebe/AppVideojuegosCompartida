package com.victhor.appvideojuegos.domain.usecase

import androidx.lifecycle.LiveData
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego

class VideojuegoUseCase(private val repositorio: VideojuegoRepository) {

    fun obtenerListaVideojuegos(): LiveData<List<Videojuego>> {
        return repositorio.listarVideojuegos
    }

    suspend fun insertarVideojuego(videojuego: Videojuego) {
        repositorio.insertarVideojuego(videojuego)
    }

    suspend fun modificarVideojuego(videojuego: Videojuego) {
        repositorio.modificarVideojuego(videojuego)
    }

    suspend fun eliminarVideojuego(videojuego: Videojuego) {
        repositorio.eliminarVideojuego(videojuego)
    }

    fun buscarVideojuegoPorId(id: Int): LiveData<Videojuego> =
        repositorio.buscarVideojuegoPorId(id)}
