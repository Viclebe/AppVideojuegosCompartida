package com.victhor.appvideojuegos.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.victhor.appvideojuegos.data.local.dao.VideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity
import com.victhor.appvideojuegos.domain.model.Videojuego

//---Clase intermediaria entre BBDD y domain/UI
//Transfiere las entidades
class VideojuegoRepository(private val dao: VideojuegoDAO) {

    //LiveData para la lista de videojuegos, convierte videojuegoEntity en VIdeojuego
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

    //Insertar en la BBDD
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

    //Modificar
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

    //Eliminar
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

    //Buscar videojuego por id (para detalles y modificar)
    fun buscarVideojuegoPorId(id: Int): LiveData<Videojuego> {
        return dao.obtenerVideojuegoPorId(id).map {
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

    //Buscar videojuego por texto (para buscar)
    fun buscarVideojuego(texto: String): LiveData<List<Videojuego>> {
        return dao.buscarVideojuegos(texto).map { entidades ->
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
    }

    //Estadísticas
    fun contarVideojuegos(): LiveData<Int> = dao.obtenerSumaVideojuegos()
    fun contarPorEstado(estado: String): LiveData<Int> = dao.obtenerSumaPorEstado(estado)
    fun mediaValoracion(): LiveData<Double> = dao.obtenerMediaValoracion()
    fun contarHorasJugadas(): LiveData<Int> = dao.obtenerHorasTotales()

    //Eliminar todo
    fun eliminarTodo() = dao.eliminarTodaBiblioteca()
}