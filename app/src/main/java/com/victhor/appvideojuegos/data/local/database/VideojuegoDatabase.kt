package com.victhor.appvideojuegos.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.victhor.appvideojuegos.data.local.dao.VideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity

@Database(entities = [VideojuegoEntity::class], version = 1)
abstract class VideojuegoDatabase : RoomDatabase() {

    abstract fun videojuegoDao(): VideojuegoDAO

    companion object {

        private var instancia: VideojuegoDatabase? = null

        fun obtenerInstancia(context: Context): VideojuegoDatabase {
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    context.applicationContext,
                    VideojuegoDatabase::class.java,
                    "videojuego_database"
                ).build().also {
                    instancia = it
                }
            }
        }
    }
}


