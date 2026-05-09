package com.victhor.appvideojuegos.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.victhor.appvideojuegos.data.local.entity.UsuarioVideojuegoEntity
import kotlinx.coroutines.flow.Flow

/**
 * UsuarioVideojuegoDAO obtiene la información directamente de la BBDD mediante consultas para la entidad UsuarioVideojuego.
 * Operaciones CRUD Insertar/actualizar, Obtener progreso, favoritos, y filtrar por estado y estadísticas.
 */
@Dao
interface UsuarioVideojuegoDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertarUsuarioVideojuego(usuarioVideojuego: UsuarioVideojuegoEntity)

    /**
     * Obtener mi progreso: Una @Query para obtener el UsuarioVideojuegoEntity de
     * un usuarioId y un videojuegoId concretos (para pintar el Detalle).
     */
    @Query("SELECT * FROM usuarios_videojuegos WHERE usuarioId = :usuarioId AND videojuegoId = :videojuegoId")
    fun obtenerProgresoPersonal(
        usuarioId: String,
        videojuegoId: Int
    ): Flow<UsuarioVideojuegoEntity>

    /**
     * Mis favoritos: Una @Query que devuelva todos los registros donde
     * favorito = 1 (SQLite guarda booleanos como 1 y 0) de un usuarioId.
     */
    @Query("SELECT * FROM usuarios_videojuegos WHERE usuarioId = :usuarioId AND favorito=1")
    fun obtenerFavoritos(usuarioId: String): Flow<UsuarioVideojuegoEntity>

    //-------------Estadísticas---------------------
    /**
     * Número total de videojuegos según estado (completado, pendiente o jugando).
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param estado del videojuego en la tabla intermedia.
     * @param usuarioId id del usuario.
     * @return Int total de videojuegos con ese estado.
     */
    @Query("SELECT COUNT(*) FROM usuarios_videojuegos WHERE estado = :estado AND usuarioId = :usuarioId")
    fun obtenerSumaPorEstado(estado: String, usuarioId: String): Flow<Int>

    /**
     * Suma total de las horas jugadas de todos los juegos del usuario.
     */
    @Query("SELECT SUM(horasJugadas) FROM usuarios_videojuegos WHERE usuarioId = :usuarioId")
    fun obtenerSumaTotalHoras(usuarioId: String): Flow<Int?>

    /**
     * Obtener toda la lista de progresos de un usuario para combinarlos en el repositorio.
     */
    @Query("SELECT * FROM usuarios_videojuegos WHERE usuarioId = :usuarioId")
    fun obtenerTodosLosProgresos(usuarioId: String): Flow<List<UsuarioVideojuegoEntity>>
}

