package com.victhor.appvideojuegos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad Videojuego, define la tabla videojuego dentro de la base de datos local en Room.
 * Cada instancia de esta clase corresponde a un registro en la base de datos.
 * usuarioId permitirá que cada usuario tenga su propia biblioteca de videojuegos.
 */
@Entity(tableName = "videojuegos")
data class VideojuegoEntity(
    @PrimaryKey(autoGenerate = true) //Clave Pirmaria autogenerada
    val id: Int = 0,
    val titulo: String,
    val genero: String, //Acción, Aventura, Rol, Estrategia, Simulación, Plataformas, Deportes, Puzzle y Arcade
    val plataforma: String, //PS5, Xbox, Nintendo Switch, PC
    val estado: String, //Jugando, pendiente, finalizado
    val horasJugadas: Int,
    val valoracion: Double, // 0 a 5
    //Clave foránea
    val usuarioId: String //Relación videojuego-usuario
)