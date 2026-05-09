package com.victhor.appvideojuegos.domain.model

/**
 * Modelo de dominio que representa la tabla usuario_videojuego.
 * Transportar información entre Repository y la UI.
 */
data class UsuarioVideojuego(
    val usuarioId:String="",
    val videojuegoId:Int=0,
    val estado:String="",
    val favorito:Boolean=false,
    val horasJugadas:Int = 0,
    val fechaInicio:Long=0,
    val fechaFin:Long=0
)
