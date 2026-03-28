package com.victhor.appvideojuegos.data.repository

import com.victhor.appvideojuegos.data.local.dao.VideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Repositorio: clase intermediaria entre BBDD DAO y UI.
 * Recoge los datos y convierte las entidades en objetos (modelos de dominio).
 *
 * Las funciones suspend porque realiza una operación de base de datos que puede tardar
 * y debe ejecutarse dentro de una coroutine (viewModelScope.launch) para no bloquear la UI (hilos).
 */
class VideojuegoRepository(private val dao: VideojuegoDAO) {

    /**
     * Listar videojuegos, convierte videojuegoEntity en un objeto Videojuego para devolver la lista.
     *
     * @return List tipo Flow con los videojuegos de un usuario (usuarioId).
     */
    fun listarVideojuegos(): Flow<List<Videojuego>> {
        // Llamada al DAO para obtener videojuegos del usuarioId
        return dao.obtenerVideojuegosPorUsuarioId(Sesion.usuarioId)
            .map { entidades -> // Transforma la lista de entidades
                entidades.map { // convertir Entity en objeto
                    Videojuego(
                        id = it.id,
                        titulo = it.titulo,
                        genero = it.genero,
                        plataforma = it.plataforma,
                        estado = it.estado,
                        horasJugadas = it.horasJugadas,
                        valoracion = it.valoracion,
                        usuarioId = it.usuarioId
                    )
                }
            }
    }

    /**
     * Insertar videojuego, funciona al revés, convierte el objeto videojuego en una Entity.
     *
     * @param videojuego insertado.
     */
    suspend fun insertarVideojuego(videojuego: Videojuego) {
        dao.insertar( //Convertir el objeto en entity para guardarlo en Room
            VideojuegoEntity(
                id = videojuego.id,
                titulo = videojuego.titulo,
                genero = videojuego.genero,
                plataforma = videojuego.plataforma,
                estado = videojuego.estado,
                horasJugadas = videojuego.horasJugadas,
                valoracion = videojuego.valoracion,
                usuarioId = Sesion.usuarioId
            )
        )
    }

    /**
     * Actualizar videojuego existente. Convierte de objeto videojuego a entity.
     *
     * @param videojuego con los datos actualizados.
     */
    suspend fun modificarVideojuego(videojuego: Videojuego) {
        dao.modificar( //Convertir el objeto en entity para modificarlo en Room
            VideojuegoEntity(
                id = videojuego.id,
                titulo = videojuego.titulo,
                genero = videojuego.genero,
                plataforma = videojuego.plataforma,
                estado = videojuego.estado,
                horasJugadas = videojuego.horasJugadas,
                valoracion = videojuego.valoracion,
                usuarioId = Sesion.usuarioId
            )
        )
    }

    /**
     * Eliminar videojuego de la BD.
     *
     * @param videojuego a eliminar.
     */
    suspend fun eliminarVideojuego(videojuego: Videojuego) {
        dao.eliminar(
            VideojuegoEntity( //Convertir el objeto en entity para eliminarlo en Room
                id = videojuego.id,
                titulo = videojuego.titulo,
                genero = videojuego.genero,
                plataforma = videojuego.plataforma,
                estado = videojuego.estado,
                horasJugadas = videojuego.horasJugadas,
                valoracion = videojuego.valoracion,
                usuarioId = Sesion.usuarioId
            )
        )
    }

    /**
     * Buscar videojuego por el id del usuario (para detalles y modificar).
     * Convierte videojuegoEntity en un objeto Videojuego tipo Flow.
     *
     * @param id del videojuego.
     * @return Videojuego encontrado.
     */
    fun buscarVideojuegoPorId(id: Int): Flow<Videojuego> {
        return dao.obtenerVideojuegoPorId(id, Sesion.usuarioId)
            .map { // Transforma la lista de entidades y convertir Entity en objeto
                Videojuego(
                    id = it.id,
                    titulo = it.titulo,
                    genero = it.genero,
                    plataforma = it.plataforma,
                    estado = it.estado,
                    horasJugadas = it.horasJugadas,
                    valoracion = it.valoracion,
                    usuarioId = it.usuarioId
                )
            }
    }

    /**
     * Buscar videojuego por texto (para buscar por título, género, platadorma o estado).
     * Convierte videojuegoEntity en un objeto Videojuego para devolver la lista.
     *
     * @param texto que será introducido en la UI.
     * @return List tipo Flow de videojuegos con coincidencia.
     */
    fun buscarVideojuego(texto: String): Flow<List<Videojuego>> {
        return dao.buscarVideojuegos(Sesion.usuarioId, texto)
            .map { entidades -> // Transforma la lista de entidades
                entidades.map { // Convertir Entity en objeto
                    Videojuego(
                        id = it.id,
                        titulo = it.titulo,
                        genero = it.genero,
                        plataforma = it.plataforma,
                        estado = it.estado,
                        horasJugadas = it.horasJugadas,
                        valoracion = it.valoracion,
                        usuarioId = it.usuarioId
                    )
                }
            }
    }

    /**
     * Eliminar biblioteca entera del usuario actual (usuarioId).
     */
    suspend fun eliminarTodo() {
        dao.eliminarTodaBiblioteca(Sesion.usuarioId)
    }

    //-------------------Estadísticas-------------

    /**
     * Número total de videojuegos del usuario (usuarioId).
     */
    fun contarVideojuegos(): Flow<Int> = dao.obtenerSumaVideojuegos(Sesion.usuarioId)

    /**
     * Número total de videojuegos por estado (jugando, pendiente, finalizado), de un usuario (usuarioId).
     *
     * @param estado del videojuego.
     */
    fun contarPorEstado(estado: String): Flow<Int> =
        dao.obtenerSumaPorEstado(estado, Sesion.usuarioId)

    /**
     * Valoración media de la biblioteca del usuario (usuarioId).
     */
    fun mediaValoracion(): Flow<Double> = dao.obtenerMediaValoracion(Sesion.usuarioId)

    /**
     * Eliminar biblioteca completa de videojuegos del usuario actual (usuarioId).
     */
    fun contarHorasJugadas(): Flow<Int> = dao.obtenerHorasTotales(Sesion.usuarioId)


}