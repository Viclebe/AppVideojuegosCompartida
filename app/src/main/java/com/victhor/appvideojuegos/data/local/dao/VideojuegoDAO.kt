package com.victhor.appvideojuegos.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity
import com.victhor.appvideojuegos.domain.model.Videojuego

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
    fun buscarVideojuegoPorId(id: Int): LiveData<VideojuegoEntity>

}