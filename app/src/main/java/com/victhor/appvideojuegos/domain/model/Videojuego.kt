package com.victhor.appvideojuegos.domain.model

import com.google.firebase.firestore.Exclude

/**
 * Modelo de dominio que representa un videojuego.
 * Transportar información entre Repository y la UI.
 */
data class Videojuego(
    @get:Exclude val id: Int = 0,
    val titulo: String = "",
    val genero: String = "",
    val plataforma: String = "",
    val valoracion: Double = 0.0,
    val usuarioId: String = "",
    val estado: String = "Pendiente",
    val favorito: Boolean = false,
    val firestoreId: String = "",
    val likes: List<String> = emptyList(), // Lista de IDs de usuarios que han dado Like
    val fechaCreacionModificacion: Long = 0L
)
