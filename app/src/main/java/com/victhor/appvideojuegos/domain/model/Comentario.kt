package com.victhor.appvideojuegos.domain.model

/**
 * Modelo de dominio que representa un comentario en Firebase.
 * Cada campo tiene valor por defecto para que Firebase pueda instanciar la clase.
 */
data class Comentario(
    val firestoreId: String = "",             // ID del documento en Firebase
    val texto: String = "",                   // Texto del comentario
    val fechaComentario: Long = 0L,           // Timestamp
    val usuarioId: String = "",               // ID del usuario que comentó
    val nombreUsuario: String = "",           // Nombre del usuario (para mostrarlo directamente)
    val firestoreIdVideojuego: String = ""    // ID del videojuego en Firebase al que pertenece
)
