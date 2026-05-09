package com.victhor.appvideojuegos.data.repository

import com.victhor.appvideojuegos.data.local.dao.ComentarioDAO
import com.victhor.appvideojuegos.data.local.entity.ComentarioEntity
import com.victhor.appvideojuegos.domain.model.Comentario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.map

/**
 * ComentarioRepositorio: clase intermediaria entre BBDD DAO y UI.
 * Recoge los datos y convierte las entidades en objetos (modelos de dominio).
 *
 * Las funciones suspend porque realiza una operación de base de datos que puede tardar
 * y debe ejecutarse dentro de una coroutine (viewModelScope.launch) para no bloquear la UI (hilos).
 */
class ComentarioRepository(private val dao: ComentarioDAO) {
    /**
     * Insertar comentario, convierte el objeto Comentario en una Entity.
     *
     * @param comentario insertado.
     */
    suspend fun insertarComentario(comentario: Comentario) {
        dao.insertarComentario(
            ComentarioEntity(
                comentarioId = comentario.firestoreId.toIntOrNull() ?: 0,
                texto = comentario.texto,
                fechaComentario = comentario.fechaComentario,
                usuarioId = comentario.usuarioId,
                videojuegoId = comentario.firestoreIdVideojuego.toIntOrNull() ?: 0
            )
        )
    }

    /**
     * Obtener comentario por el id de un videojuego pasado como parámetro.
     * Convierte comentarioEntity en un objeto Comentario tipo Flow.
     *
     * @param videojuegoId del usuario.
     * @return Usuario encontrado.
     */
    fun obtenerComentarioPorVideojuegoId(videojuegoId: Int): Flow<List<Comentario>> {
        return dao.obtenerComentarioPorVideojuego(videojuegoId)
            .map { entidades -> // Transforma la lista de entidades
                entidades.map { // Convertir Entity en objeto
                    Comentario(
                        firestoreId = it.comentarioId.toString(),
                        texto = it.texto,
                        fechaComentario = it.fechaComentario,
                        usuarioId = it.usuarioId,
                        firestoreIdVideojuego = it.videojuegoId.toString(),
                        nombreUsuario = "" // Firebase lo maneja en la nube
                    )
                }
            }
    }

    /**
     * Modificar comentario propio. Convierte de objeto comentario a entity.
     *
     * @param ccomentario con los datos actualizados.
     */
    suspend fun modificarComentario(comentario: Comentario) {
        dao.modificarComentarioPropio( //Convertir el objeto en entity para modificarlo en Room
            ComentarioEntity(
                comentarioId = comentario.firestoreId.toIntOrNull() ?: 0,
                texto = comentario.texto,
                fechaComentario = comentario.fechaComentario,
                usuarioId = comentario.usuarioId,
                videojuegoId = comentario.firestoreIdVideojuego.toIntOrNull() ?: 0
            )
        )
    }

    /**
     * Eliminar videojuego de la BD, buscado por el id del usuario.
     *
     * @param comentario a eliminar.
     */
    suspend fun eliminarComentarioPropio(comentario: Comentario) {
        dao.eliminarComentarioPropio(
            ComentarioEntity( //Convertir el objeto en entity para eliminarlo en Room
                comentarioId = comentario.firestoreId.toIntOrNull() ?: 0,
                texto = comentario.texto,
                fechaComentario = comentario.fechaComentario,
                usuarioId = comentario.usuarioId,
                videojuegoId = comentario.firestoreIdVideojuego.toIntOrNull() ?: 0
            )
        )
    }
}
