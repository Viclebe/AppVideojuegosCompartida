package com.victhor.appvideojuegos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "videojuegos")
data class VideojuegoEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val titulo: String,
    val genero: String, //Acción, Aventura, Rol, Estrategia, Simulación, Plataformas, Deportes, Puzzle y Arcade
    val plataforma: String, //PS5, Xbox, Nintendo Switch, PC
    val estado: String, //Jugando, pendiente, finalizado
    val horasJugadas: Int,
    val valoracion: Double // de 1 a 5
)