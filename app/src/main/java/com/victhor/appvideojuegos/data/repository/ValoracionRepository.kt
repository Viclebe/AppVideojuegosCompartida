package com.victhor.appvideojuegos.data.repository

import com.victhor.appvideojuegos.data.local.dao.ValoracionDAO
import com.victhor.appvideojuegos.data.local.entity.ValoracionEntity
import com.victhor.appvideojuegos.domain.model.Valoracion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * ValoracionRepositorio: clase intermediaria entre BBDD DAO y UI.
 * Recoge los datos y convierte las entidades en objetos (modelos de dominio).
 *
 * Las funciones suspend porque realiza una operación de base de datos que puede tardar
 * y debe ejecutarse dentro de una coroutine (viewModelScope.launch) para no bloquear la UI (hilos).
 */
class ValoracionRepository(private val dao: ValoracionDAO) {
    //obtenerMediaGlobal(videojuegoId: Int): Flow<Double>, contarVotos(videojuegoId: Int): Flow<Int>

    /**
     * Insertar valoración, convierte el objeto valoración en una Entity.
     *
     * @param valoracion insertado.
     */
    suspend fun insertarValoracion(valoracion: Valoracion) {
        dao.insertarValoracion( // Convertir el objeto en entity para guardarlo en Room
            ValoracionEntity(
                valoracionId = valoracion.valoracionId,
                usuarioId = valoracion.usuarioId,
                videojuegoId = valoracion.videojuegoId,
                puntuacion = valoracion.puntuacion
            )
        )
    }

    /**
     * Calcular media global de las valoraciones.
     *
     * @param videojuegoId insertado.
     */
    fun calcularMediaGlobal(videojuegoId: Int): Flow<Double> {
        // ? porque AVG() puede devolver null
        return dao.obtenerMediaGlobal(videojuegoId).map { it ?: 0.0 }
    }

    /**
     * Contar los votos por videojuegoId.
     *
     * @param videojuegoId insertado.
     */
    fun contarVotos(videojuegoId: Int): Flow<Int> {
        return dao.contarVotos(videojuegoId)
    }
}
