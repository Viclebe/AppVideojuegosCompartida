package com.victhor.appvideojuegos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity
import kotlinx.coroutines.flow.Flow

//---DAO obtiene la información directamente de la BBDD mediante consultas---
@Dao
interface VideojuegoDAO {
    @Query("SELECT * FROM videojuegos ORDER BY titulo")
    fun obtenerTodosVideojuegos(): Flow<List<VideojuegoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(videojuego: VideojuegoEntity)

    @Update
    suspend fun modificar(videojuego: VideojuegoEntity)

    @Delete
    suspend fun eliminar(videojuego: VideojuegoEntity)

    @Query("SELECT * FROM videojuegos WHERE id = :id")
    fun obtenerVideojuegoPorId(id: Int): Flow<VideojuegoEntity>

    //Query para búsqueda
    @Query(
        "SELECT * FROM videojuegos WHERE titulo LIKE '%' || :texto || '%'OR genero LIKE '%' || :texto || '%'OR plataforma LIKE '%' || :texto || '%'OR estado LIKE '%' || :texto || '%'"
    )
    fun buscarVideojuegos(texto: String): Flow<List<VideojuegoEntity>>

    //Querys para las estadísticas
    @Query("SELECT COUNT(*) FROM videojuegos")
    fun obtenerSumaVideojuegos(): Flow<Int>

    @Query("SELECT COUNT(*) FROM videojuegos WHERE estado = :estado")
    fun obtenerSumaPorEstado(estado: String): Flow<Int>

    @Query("SELECT AVG(valoracion) FROM videojuegos")
    fun obtenerMediaValoracion(): Flow<Double>

    @Query("SELECT SUM(horasJugadas) FROM videojuegos")
    fun obtenerHorasTotales(): Flow<Int>

    //Eliminar toda la biblioteca
    @Query("DELETE FROM videojuegos")
    fun eliminarTodaBiblioteca()
}