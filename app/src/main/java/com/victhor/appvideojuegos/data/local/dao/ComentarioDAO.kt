package com.victhor.appvideojuegos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.victhor.appvideojuegos.data.local.entity.ComentarioEntity
import com.victhor.appvideojuegos.domain.model.Comentario
import kotlinx.coroutines.flow.Flow

/**
 * ComentarioDAO obtiene la información directamente de la BBDD mediante consultas para la entidad comentario.
 * Operaciones CRUD Insertar, eliminar y Modificar comentarios.
 */
@Dao
interface ComentarioDAO {
    /**
     * Insertar comentario en la base de datos.
     * ComentarioEntity es el objeto a insertar.
     *
     * @param ComentarioEntity
     */
    @Insert
    suspend fun insertarComentario(comentario: ComentarioEntity)

    /**
     * Modificar comentario en la base de datos.
     * ComentarioEntity es el objeto a insertar.
     *
     * @param ComentarioEntity
     */
    @Update
    suspend fun modificarComentarioPropio(comentario: ComentarioEntity)

    /**
     * Eliminar comentario en la base de datos.
     * ComentarioEntity es el objeto a insertar.
     *
     * @param ComentarioEntity
     */
    @Delete
    suspend fun eliminarComentarioPropio(comentario: ComentarioEntity)

    /**
     * Devolver comentario por el Id de un videjuego.
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param videojuegoId
     * @return comentario o null si no lo encuentra.
     */
    @Query("SELECT * FROM comentarios WHERE videojuegoId = :videojuegoId")
    fun obtenerComentarioPorVideojuego(videojuegoId: Int): Flow<List<ComentarioEntity>>

}