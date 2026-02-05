package com.victhor.appvideojuegos.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.victhor.appvideojuegos.data.local.dao.VideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity
import com.victhor.appvideojuegos.domain.model.Videojuego

class VideojuegoRepository(private val dao: VideojuegoDAO) {
    val listarVideojuegos: LiveData<List<Videojuego>> =
        dao.obtenerTodosVideojuegos().map { entidades ->
            entidades.map {
                Videojuego(
                    id = it.id,
                    titulo = it.titulo,
                    genero = it.genero,
                    plataforma = it.plataforma,
                    estado = it.estado,
                    horasJugadas = it.horasJugadas,
                    valoracion = it.valoracion
                )
            }
        }

    suspend fun insertarVideojuego(videojuego: Videojuego) {
        dao.insertar(
            VideojuegoEntity(
                id = videojuego.id,
                titulo = videojuego.titulo,
                genero = videojuego.genero,
                plataforma = videojuego.plataforma,
                estado = videojuego.estado,
                horasJugadas = videojuego.horasJugadas,
                valoracion = videojuego.valoracion
            )
        )
    }

    suspend fun modificarVideojuego(videojuego: Videojuego) {
        dao.modificar(
            VideojuegoEntity(
                id = videojuego.id,
                titulo = videojuego.titulo,
                genero = videojuego.genero,
                plataforma = videojuego.plataforma,
                estado = videojuego.estado,
                horasJugadas = videojuego.horasJugadas,
                valoracion = videojuego.valoracion
            )
        )
    }

    suspend fun eliminarVideojuego(videojuego: Videojuego) {
        dao.eliminar(
            VideojuegoEntity(
                id = videojuego.id,
                titulo = videojuego.titulo,
                genero = videojuego.genero,
                plataforma = videojuego.plataforma,
                estado = videojuego.estado,
                horasJugadas = videojuego.horasJugadas,
                valoracion = videojuego.valoracion
            )
        )
    }

    fun buscarVideojuegoPorId(id: Int): LiveData<Videojuego> {
        return dao.buscarVideojuegoPorId(id).map {
            Videojuego(
                id = it.id,
                titulo = it.titulo,
                genero = it.genero,
                plataforma = it.plataforma,
                estado = it.estado,
                horasJugadas = it.horasJugadas,
                valoracion = it.valoracion
            )
        }
    }


}