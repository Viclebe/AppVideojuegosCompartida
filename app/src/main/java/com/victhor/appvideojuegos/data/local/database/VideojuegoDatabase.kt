package com.victhor.appvideojuegos.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.victhor.appvideojuegos.data.local.dao.VideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity

//---BBDD Room---

@Database(entities = [VideojuegoEntity::class], version = 1)
abstract class VideojuegoDatabase : RoomDatabase() {

    //Método DAO, Room se encarga de implementar automáticamente
    abstract fun videojuegoDao(): VideojuegoDAO

    companion object {

        //Instancia única para evitar duplicidades
        private var instancia: VideojuegoDatabase? = null

        //Obtener instancia de la BBDD de forma sincronizada
        fun obtenerInstancia(context: Context): VideojuegoDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    VideojuegoDatabase::class.java,
                    "videojuego_database"
                ).build().also {
                    //Guardar instancia
                    instancia = it
                }
            }
        }
    }
}


