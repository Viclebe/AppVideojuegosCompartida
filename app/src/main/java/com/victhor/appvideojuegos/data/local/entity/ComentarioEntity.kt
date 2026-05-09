package com.victhor.appvideojuegos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Comentario, define la tabla comentarios dentro de la base de datos local en Room.
 * Cada instancia de esta clase corresponde a un registro en la base de datos.
 */
@Entity(tableName = "comentarios")
data class ComentarioEntity(
    @PrimaryKey(autoGenerate = true)
    val comentarioId: Int = 0,
    val texto: String = "",
    val fechaComentario: Long = 0, //Timestamp
    val usuarioId: String = "", //Clave foránea. Relación Usuario - Comentario
    val videojuegoId: Int = 0 //Clave foránea. Relación Videojuego - Comentario
)
