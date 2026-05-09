package com.victhor.appvideojuegos.data.local.entity

import androidx.compose.ui.input.pointer.PointerId
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Valoracion, define la tabla valoracion dentro de la base de datos local en Room.
 * Cada instancia de esta clase corresponde a un registro en la base de datos.
 * usuarioId permitirá que cada usuario tenga su propia biblioteca de videojuegos.
 */
@Entity(
    tableName = "valoraciones",
    indices = [androidx.room.Index(value = ["usuarioId", "videojuegoId"], unique = true)]
)
data class ValoracionEntity(
    @PrimaryKey(autoGenerate = true)
    val valoracionId: Int = 0,
    val usuarioId: String = "",
    val videojuegoId: Int = 0,
    val puntuacion: Int = 0
)
