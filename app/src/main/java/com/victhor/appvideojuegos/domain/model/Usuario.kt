package com.victhor.appvideojuegos.domain.model

/**
 * Modelo de dominio que representa un usuario.
 * Transportar información entre Repository y la UI.
 */
data class Usuario(
    val uid: String = "", //uid es una cadena de Firebase
    val nombre: String = "",
    val email: String = "",
    val fechaRegistro: Long = 0, //Timestamp
    val avatarUrl: String? = null //URL de la imagen, puede ser null
)