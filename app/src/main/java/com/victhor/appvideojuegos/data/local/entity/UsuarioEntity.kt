package com.victhor.appvideojuegos.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entidad Usuario, define la tabla usuarios dentro de la base de datos local en Room.
 * Cada instancia de esta clase corresponde a un registro en la base de datos.
 */
@Entity(tableName = "usuarios", indices = [Index(value = ["email"], unique = true)])
// Crear un índice único sobre columna email (SQLite no permitirá 2 filas con el mismo mail)
data class UsuarioEntity(
    @PrimaryKey
    val uid: String, //Uid lo proporciona Firebase Authentication
    val nombre: String,
    val email: String,
    val fechaRegistro: Long, //Timestamp (milisegundos desde el 01/01/1970)
    val avatarUrl: String? = null //URL de la imagen, puede ser null
)