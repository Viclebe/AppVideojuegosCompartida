package com.victhor.appvideojuegos.domain.model

/**
 * Modelo de dominio que representa una valoración.
 * Transportar información entre Repository y la UI.
 */
data class Valoracion(
    val valoracionId: Int = 0,
    val usuarioId: String = "",
    val videojuegoId: Int = 0,
    val puntuacion: Int = 0
)
