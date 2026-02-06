package com.victhor.appvideojuegos.domain.usecase

import androidx.lifecycle.LiveData
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.domain.model.Videojuego

class VideojuegoUseCase(private val repositorio: VideojuegoRepository) {

    fun obtenerListaVideojuegos(): LiveData<List<Videojuego>> {
        return repositorio.listarVideojuegos
    }

    fun obtenerListaVideojuegosPorGenero(genero: String): LiveData<List<Videojuego>> {
        return repositorio.filtrarVideojuegoPorGenero(genero)
    }

    fun obtenerListaVideojuegosPorPlataforma(plataforma: String): LiveData<List<Videojuego>> {
        return repositorio.filtrarVideojuegoPorPlataforma(plataforma)
    }

    fun obtenerListaVideojuegosPorEstado(estado: String): LiveData<List<Videojuego>> {
        return repositorio.filtrarVideojuegoPorEstado(estado)
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

    fun buscarVideojuegos(texto: String): LiveData<List<Videojuego>> {
        return repositorio.buscarVideojuego(texto)
    }

    fun buscarVideojuegoPorId(id: Int): LiveData<Videojuego> =
        repositorio.buscarVideojuegoPorId(id)

    //cÁLCULO   estadísticas
    fun totalVideojuegos(): LiveData<Int> = repositorio.contarVideojuegos()
    fun totalVideojuegosJugando(): LiveData<Int> = repositorio.contarPorEstado("Jugando")
    fun totalVideojuegosPendiente(): LiveData<Int> = repositorio.contarPorEstado("Pendiente")
    fun totalVideojuegosFinalizado(): LiveData<Int> = repositorio.contarPorEstado("Finalizado")
    fun mediaValoracion(): LiveData<Double> = repositorio.mediaValoracion()
    fun totalHoras(): LiveData<Int> = repositorio.contarHorasJugadas()

    //Eliminar toda la biblioteca
    fun eliminarBiblioteca() = repositorio.eliminarTodo()


}
