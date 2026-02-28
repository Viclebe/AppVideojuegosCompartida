package com.victhor.appvideojuegos.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

//
@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val uid: String,
    val email: String,
    val password: String
)