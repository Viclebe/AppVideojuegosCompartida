package com.victhor.appvideojuegos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.victhor.appvideojuegos.data.local.entity.ValoracionEntity
import kotlinx.coroutines.flow.Flow

/**
 * ValoracionDAO obtiene la información directamente de la BBDD mediante consultas para la entidad valoracion.
 * Operaciones CRUD Insertar, eliminar y Modificar comentarios.
 */
@Dao
interface ValoracionDAO {
    /**
     * Insertar valoración en la base de datos.
     * ValoracionEntity es el objeto a insertar.
     *
     * @param ValoracionEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) // si ya existe, reemplaza la valoración
    suspend fun insertarValoracion(valoracion: ValoracionEntity)

    /**
     * Obtener la media de la valoración global de un videojuego.
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param videojuegoid
     * @return Double con la media de la valoración.
     */
    @Query("SELECT AVG(puntuacion) FROM valoraciones WHERE videojuegoId = :videojuegoId")
    fun obtenerMediaGlobal(videojuegoId: Int): Flow<Double?>

    /**
     * Contar los votos totales sobre un videojuego.
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param videojuegoId
     * @return Int con el recuento total de votos.
     */
    @Query("SELECT COUNT(*) FROM valoraciones WHERE videojuegoId = :videojuegoId")
    fun contarVotos(videojuegoId: Int): Flow<Int>


}