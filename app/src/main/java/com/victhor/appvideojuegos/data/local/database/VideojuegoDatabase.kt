package com.victhor.appvideojuegos.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.victhor.appvideojuegos.data.local.dao.ComentarioDAO
import com.victhor.appvideojuegos.data.local.dao.UsuarioDAO
import com.victhor.appvideojuegos.data.local.dao.UsuarioVideojuegoDAO
import com.victhor.appvideojuegos.data.local.dao.ValoracionDAO

import com.victhor.appvideojuegos.data.local.dao.VideojuegoDAO
import com.victhor.appvideojuegos.data.local.entity.ComentarioEntity
import com.victhor.appvideojuegos.data.local.entity.UsuarioEntity
import com.victhor.appvideojuegos.data.local.entity.UsuarioVideojuegoEntity
import com.victhor.appvideojuegos.data.local.entity.ValoracionEntity

import com.victhor.appvideojuegos.data.local.entity.VideojuegoEntity

/**
 * Configuración de la base de datos principal. Llamadas a los DAO.
 * Con Room se genera automáticamente la implementación en tiempo de compilación.
 * Crea las database a partir de las entidades UsuarioEntity y VideojuegoEntity.
 */
@Database(
    entities = [VideojuegoEntity::class, UsuarioEntity::class, ComentarioEntity::class, ValoracionEntity::class, UsuarioVideojuegoEntity::class],
    version = 9
)
abstract class VideojuegoDatabase : RoomDatabase() {

    /**
     * Acceso a los DAOs con Room.
     *
     * @return instanciasde los DAOs.
     */
    abstract fun videojuegoDao(): VideojuegoDAO
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun comentarioDao(): ComentarioDAO
    abstract fun valoracionDao(): ValoracionDAO
    abstract fun usuarioVideojuegoDao(): UsuarioVideojuegoDAO


    companion object { //Objeto estático para almacenar elementos accesibles sin crear la clase

        //Variable para Método Singleton: almacena la instancia única de la base de datos para evitar duplicidades.
        private var instancia: VideojuegoDatabase? = null

        /**
         * Migración 2->3 para añadir usuarioId a videojuegos (preparación para interactuar con bibliotecas externas).
         * Añadir la columna usuarioId a la tabla Videojuegos, permite asociar cada videojuego a un usuario específico.
         */
        val MIGRATION_2_3 = object : Migration(2, 3) {

            // Método para migraciones de bases de datos
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL( // Consulta SQL para añadir columna usuarioId a la tabla Videojuegos
                    """
                    ALTER TABLE videojuegos 
                    ADD COLUMN usuarioId TEXT NOT NULL DEFAULT ''
                    """
                )
            }
        }

        /**
         * Obtener instancia de la BBDD de forma sincronizada.
         * Patrón Singleton garantiza que sólo exista una instancia de la base de datos durante todo
         * el ciclo de vida de la aplicación.
         *
         * @param context Contexto de la aplicación.
         * @return Instancia de VideojuegoDatabase.
         */
        fun obtenerInstancia(context: Context): VideojuegoDatabase {

            //Si la instancia existe la devuelve, si null, la crea sincronizada (evita problemas de hilos)
            return instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder( //Si instancia = null, crear base de datos Room
                    context.applicationContext, //Contexto
                    VideojuegoDatabase::class.java, //Clase
                    "videojuego_database" //Nombre
                ).addMigrations(MIGRATION_2_3) //Añadir migración
                    .fallbackToDestructiveMigration() //Si falla la migración, eliminar la base, evita que crashee
                    .build().also { //Construir instancia
                        instancia = it //Guardar instancia
                    }
            }
        }
    }
}


