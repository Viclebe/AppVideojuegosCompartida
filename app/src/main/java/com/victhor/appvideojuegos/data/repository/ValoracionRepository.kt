package com.victhor.appvideojuegos.data.repository

import com.victhor.appvideojuegos.data.local.dao.ValoracionDAO
import kotlinx.coroutines.flow.Flow

/**
 * ValoracionRepositorio: clase intermediaria entre BBDD DAO y UI.
 * Recoge los datos y convierte las entidades en objetos (modelos de dominio).
 *
 * Las funciones suspend porque realiza una operación de base de datos que puede tardar
 * y debe ejecutarse dentro de una coroutine (viewModelScope.launch) para no bloquear la UI (hilos).
 */
class ValoracionRepository(private val dao: ValoracionDAO) {
    /**
     * Contar los votos por videojuegoId.
     *
     * @param videojuegoId insertado.
     */
    fun contarVotos(videojuegoId: Int): Flow<Int> {
        return dao.contarVotos(videojuegoId)
    }
}
