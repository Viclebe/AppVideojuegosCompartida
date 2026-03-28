package com.victhor.appvideojuegos.domain.model

/**
 * Modelo de dominio que representa un videojuego.
 * Transportar información entre Repository y la UI.
 */
data class Videojuego(
    val id: Int = 0,
    val titulo: String,
    val genero: String,
    val plataforma: String,
    val estado: String,
    val horasJugadas: Int,
    val valoracion: Double,
    val usuarioId: String
)
