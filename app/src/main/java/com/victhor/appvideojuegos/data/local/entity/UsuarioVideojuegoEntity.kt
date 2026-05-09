package com.victhor.appvideojuegos.data.local.entity

import androidx.room.Entity

/**
 * Entidad Usuario-Videojuego. Para que los Usuario que comparten objetos videojeugo en sus bibliotecas, no tengan el mismo estado.
 * Cada instancia de esta clase corresponde a un registro en la base de datos.
 */
@Entity(tableName = "usuarios_videojuegos", primaryKeys = ["usuarioId","videojuegoId"])
data class UsuarioVideojuegoEntity(
    val usuarioId: String = "",
    val videojuegoId: Int = 0,
    val estado: String = "",
    val favorito: Boolean = false,
    val horasJugadas: Int = 0,
    val fechaInicio: Long = 0,
    val fechaFin: Long = 0
)
