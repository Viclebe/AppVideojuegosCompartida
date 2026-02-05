package com.victhor.appvideojuegos.domain.model

data class Videojuego(
    val id: Int = 0,
    val titulo: String,
    val genero: String,
    val plataforma: String,
    val estado: String,
    val horasJugadas: Int,
    val valoracion: Double
)