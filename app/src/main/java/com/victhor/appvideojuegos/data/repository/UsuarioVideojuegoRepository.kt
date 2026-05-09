package com.victhor.appvideojuegos.data.repository

import com.victhor.appvideojuegos.data.local.dao.UsuarioVideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.UsuarioVideojuegoEntity
import com.victhor.appvideojuegos.domain.model.UsuarioVideojuego
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * Guardar y leer los progresos, los favoritos y las horas que el usuario invierte en un juego para comunicárselos a las pantallas de la app.
 * Repositorio: clase intermediaria entre DAO (UsuarioVideojuegoDAO) y UI.
 * Transporta los datos de los estados (jugando, completado, etc), la puntuación y las horas de cada usuario por juego.
 */
class UsuarioVideojuegoRepository(private val dao: UsuarioVideojuegoDAO) {

    /**
     * Insertar o actualizar el progreso personal de un videojuego.
     */
    suspend fun guardarProgreso(progreso: UsuarioVideojuego) {
        dao.insertarUsuarioVideojuego(
            UsuarioVideojuegoEntity(
                usuarioId = Sesion.usuarioId,
                videojuegoId = progreso.videojuegoId,
                estado = progreso.estado,
                favorito = progreso.favorito,
                horasJugadas = progreso.horasJugadas,
                fechaInicio = progreso.fechaInicio,
                fechaFin = progreso.fechaFin
            )
        )
    }

    /**
     * Obtener el progreso personal para un juego. 
     * Convierte de Entity a Dominio. Puede ser nulo si el usuario aún no ha guardado
     * ese juego en la tabla intermedia.
     */
    fun obtenerProgreso(videojuegoId: Int): Flow<UsuarioVideojuego?> {
        return dao.obtenerProgresoPersonal(Sesion.usuarioId, videojuegoId).map { entity ->
            if (entity != null) {
                UsuarioVideojuego(
                    usuarioId = entity.usuarioId,
                    videojuegoId = entity.videojuegoId,
                    estado = entity.estado,
                    favorito = entity.favorito,
                    horasJugadas = entity.horasJugadas,
                    fechaInicio = entity.fechaInicio,
                    fechaFin = entity.fechaFin
                )
            } else {
                null
            }
        }
    }

    /**
     * Número total de videojuegos por estado (jugando, pendiente, finalizado), del usuario actual.
     */
    fun contarPorEstado(estado: String): Flow<Int> {
        return dao.obtenerSumaPorEstado(estado, Sesion.usuarioId)
    }

    /**
     * Suma total de las horas jugadas de todos los juegos del usuario.
     */
    fun contarHorasJugadas(): Flow<Int> {
        return dao.obtenerSumaTotalHoras(Sesion.usuarioId).map { it ?: 0 }
    }

}
