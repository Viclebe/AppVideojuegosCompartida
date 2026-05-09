package com.victhor.appvideojuegos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity
import kotlinx.coroutines.flow.Flow

/**
 * VideojuegoDAO obtiene la información directamente de la BBDD mediante consultas para la tabla Videojuego.
 * Room implementa los cambios automáticamente en tiempo de compilación.
 *
 * Operaciones CRUD:
 * -Insertar, actualizar y eliminar videojuegos de la biblioteca personal.
 * -Consultar videojuegos por usuario, título, género, plataforma o estado.
 * -Cáculo de las estadísticas.
 *
 */
@Dao
interface VideojuegoDAO {

    /* @Query("SELECT * FROM videojuegos ORDER BY titulo")
     fun obtenerTodosVideojuegos(): Flow<List<VideojuegoEntity>>*/

    //Query para búsqueda
    /*@Query(
        "SELECT * FROM videojuegos WHERE titulo LIKE '%' || :texto || '%'OR genero LIKE '%' || :texto || '%'OR plataforma LIKE '%' || :texto || '%'OR estado LIKE '%' || :texto || '%'"
    )
    fun buscarVideojuegos(texto: String): Flow<List<VideojuegoEntity>>*/

    /**
     * Insertar videojuego en la base de datos.
     * VideojuegoEntity es el objeto a insertar.
     *
     * @param VideojuegoEntity para insertar.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) // si ya existe, reemplaza el usuario
    suspend fun insertar(videojuego: VideojuegoEntity): Long

    /**
     * Actualizar videojuego ya creado en la base de datos.
     *
     * @param videojuego para modificar.
     */
    @Update
    suspend fun modificar(videojuego: VideojuegoEntity)

    /**
     * Eliminar un videojuego de la base de datos.
     *
     * @param videojuego para eliminar.
     */
    @Delete
    suspend fun eliminar(videojuego: VideojuegoEntity)

    /**
     * Devolver un videojuego por su id y el id de su usuario.
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param id del videojuego.
     * @param usuarioId
     * @return videojuego encontrado.
     */
    @Query("SELECT * FROM videojuegos WHERE id = :id AND usuarioId = :usuarioId")
    fun obtenerVideojuegoPorId(id: Int, usuarioId: String): Flow<VideojuegoEntity>

    /**
     * Buscar videojuegos en la biblioteca por título, género, plataforma o estado.
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param usuarioId
     * @param texto con el que realizará la búsqueda.
     * @return List de videojuegos que coincidan.
     */
    @Query("SELECT * FROM videojuegos WHERE usuarioId = :usuarioId AND (titulo LIKE '%' || :texto || '%' OR genero LIKE '%' || :texto || '%' OR plataforma LIKE '%' || :texto || '%') ")
    fun buscarVideojuegos(usuarioId: String, texto: String): Flow<List<VideojuegoEntity>>

    /**
     * Eliminar toda la biblioteca de un usuario por su id.
     *
     * @param usuarioId
     */
    @Query("DELETE FROM videojuegos WHERE usuarioId = :usuarioId")
    suspend fun eliminarTodaBiblioteca(usuarioId: String)

    @Query("SELECT * FROM videojuegos WHERE usuarioId = :usuarioId ORDER BY titulo")
    fun obtenerVideojuegosPorUsuarioId(usuarioId: String): Flow<List<VideojuegoEntity>>

    /**
     * Borrar los videojuegos de la biblioteca de un usuario buscado por su id.
     *
     * @param usuarioId
     */
    @Query("DELETE FROM videojuegos WHERE usuarioId = :usuarioId")
    suspend fun borrarVideojuegoPorUsusarioId(usuarioId: String)


    //------Querys para las estadísticas---------

    /**
     * Número total de videojuegos en la biblioteca de un usuario.
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param usuarioId
     * @return Int total de videojuegos.
     */
    @Query("SELECT COUNT(*) FROM videojuegos WHERE usuarioId = :usuarioId")
    fun obtenerSumaVideojuegos(usuarioId: String): Flow<Int>

    /**
     * Media de la valoración de todos los videojuegos de un usuario.
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param usuarioId
     * @return Double con la media.
     */
    @Query("SELECT AVG(valoracion) FROM videojuegos WHERE usuarioId = :usuarioId")
    fun obtenerMediaValoracion(usuarioId: String): Flow<Double>
}