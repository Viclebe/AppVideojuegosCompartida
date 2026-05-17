package com.victhor.appvideojuegos.data.repository

import com.victhor.appvideojuegos.data.local.dao.VideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity
import com.victhor.appvideojuegos.domain.model.Videojuego
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.combine
import com.victhor.appvideojuegos.data.local.dao.UsuarioVideojuegoDAO

/**
 * Repositorio: clase intermediaria entre BBDD DAO y UI.
 * Recoge los datos y convierte las entidades en objetos (modelos de dominio).
 *
 * Las funciones suspend porque realiza una operación de base de datos que puede tardar
 * y debe ejecutarse dentro de una coroutine (viewModelScope.launch) para no bloquear la UI (hilos).
 */
class VideojuegoRepository(
    private val dao: VideojuegoDAO,
    private val usuarioVideojuegoDao: UsuarioVideojuegoDAO
) {

    /**
     * Listar videojuegos, convierte videojuegoEntity en un objeto Videojuego para devolver la lista.
     *
     * @return List tipo Flow con los videojuegos de un usuario (usuarioId).
     */
    fun listarVideojuegos(): Flow<List<Videojuego>> {
        val juegosFlow = dao.obtenerVideojuegosPorUsuarioId(Sesion.usuarioId)
        val progresosFlow = usuarioVideojuegoDao.obtenerTodosLosProgresos(Sesion.usuarioId)

        return combine(juegosFlow, progresosFlow) { juegos, progresos ->
            juegos.map { entity ->
                val progreso = progresos.find { it.videojuegoId == entity.id }
                Videojuego(
                    id = entity.id,
                    titulo = entity.titulo,
                    genero = entity.genero,
                    plataforma = entity.plataforma,
                    valoracion = entity.valoracion,
                    usuarioId = entity.usuarioId,
                    nombreUsuario = entity.nombreUsuario,
                            estado = progreso?.estado ?: "Pendiente",
                    favorito = progreso?.favorito ?: false,
                    imagenUrl = entity.imagenUrl
                )
            }
        }
    }

    /**
     * Insertar videojuego, funciona al revés, convierte el objeto videojuego en una Entity.
     *
     * @param videojuego insertado.
     */
    suspend fun insertarVideojuego(videojuego: Videojuego): Int {
        return dao.insertar( //Convertir el objeto en entity para guardarlo en Room
            VideojuegoEntity(
                id = videojuego.id,
                titulo = videojuego.titulo,
                genero = videojuego.genero,
                plataforma = videojuego.plataforma,
                valoracion = videojuego.valoracion,
                usuarioId = Sesion.usuarioId,
                nombreUsuario = videojuego.nombreUsuario,
                imagenUrl = videojuego.imagenUrl
            )
        ).toInt()
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
                valoracion = videojuego.valoracion,
                usuarioId = Sesion.usuarioId,
                nombreUsuario = videojuego.nombreUsuario,
                imagenUrl = videojuego.imagenUrl
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
                valoracion = videojuego.valoracion,
                usuarioId = Sesion.usuarioId,
                nombreUsuario = videojuego.nombreUsuario,
                imagenUrl = videojuego.imagenUrl
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
                    valoracion = it.valoracion,
                    usuarioId = it.usuarioId,
                    nombreUsuario = it.nombreUsuario,
                    imagenUrl = it.imagenUrl
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
        val busquedaFlow = dao.buscarVideojuegos(Sesion.usuarioId, texto)
        val progresosFlow = usuarioVideojuegoDao.obtenerTodosLosProgresos(Sesion.usuarioId)

        return combine(busquedaFlow, progresosFlow) { resultados, progresos ->
            resultados.map { entity ->
                val progreso = progresos.find { it.videojuegoId == entity.id }
                Videojuego(
                    id = entity.id,
                    titulo = entity.titulo,
                    genero = entity.genero,
                    plataforma = entity.plataforma,
                    valoracion = entity.valoracion,
                    usuarioId = entity.usuarioId,
                    nombreUsuario = entity.nombreUsuario,
                            estado = progreso?.estado ?: "Pendiente",
                    favorito = progreso?.favorito ?: false,
                    imagenUrl = entity.imagenUrl
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
     * Valoración media de la biblioteca del usuario (usuarioId).
     */
    fun mediaValoracion(): Flow<Double> = dao.obtenerMediaValoracion(Sesion.usuarioId)


}