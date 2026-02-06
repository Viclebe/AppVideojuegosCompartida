package com.victhor.appvideojuegos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity

@Dao
interface VideojuegoDAO {
    @Query("SELECT * FROM videojuegos ORDER BY titulo")
    fun obtenerTodosVideojuegos(): LiveData<List<VideojuegoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertar(videojuego: VideojuegoEntity)

    @Update
    suspend fun modificar(videojuego: VideojuegoEntity)

    @Delete
    suspend fun eliminar(videojuego: VideojuegoEntity)

    @Query("SELECT * FROM videojuegos WHERE id = :id")
    fun obtenerVideojuegoPorId(id: Int): LiveData<VideojuegoEntity>

    //Querys para filtrar
    @Query("SELECT * FROM videojuegos WHERE genero = :genero")
    fun obtenerVideojuegoPorGenero(genero: String): LiveData<List<VideojuegoEntity>>

    @Query("SELECT * FROM videojuegos WHERE plataforma = :plataforma")
    fun obtenerVideojuegoPorPlataforma(plataforma: String): LiveData<List<VideojuegoEntity>>

    @Query("SELECT * FROM videojuegos WHERE estado = :estado")
    fun obtenerVideojuegoPorEstado(estado: String): LiveData<List<VideojuegoEntity>>

    //Query para búsqueda
    @Query(
        "SELECT * FROM videojuegos WHERE titulo LIKE '%' || :texto || '%'OR genero LIKE '%' || :texto || '%'OR plataforma LIKE '%' || :texto || '%'OR estado LIKE '%' || :texto || '%'"
    )
    fun buscarVideojuegos(texto: String): LiveData<List<VideojuegoEntity>>

    //Querys para las estadísticas
    @Query("SELECT COUNT(*) FROM videojuegos")
    fun obtenerSumaVideojuegos(): LiveData<Int>

    @Query("SELECT COUNT(*) FROM videojuegos WHERE estado = :estado")
    fun obtenerSumaPorEstado(estado: String): LiveData<Int>

    @Query("SELECT AVG(valoracion) FROM videojuegos")
    fun obtenerMediaValoracion(): LiveData<Double>

    @Query("SELECT SUM(horasJugadas) FROM videojuegos")
    fun obtenerHorasTotales(): LiveData<Int>

    //Eliminar toda la biblioteca
    @Query("DELETE FROM videojuegos")
    fun eliminarTodaBiblioteca()
}