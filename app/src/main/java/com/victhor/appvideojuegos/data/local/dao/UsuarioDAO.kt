package com.victhor.appvideojuegos.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.victhor.appvideojuegos.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

/**
 * UsuarioDAO obtiene la información directamente de la BBDD mediante consultas para la entidad usuario.
 * Operaciones CRUD Insertar, eliminar y consultar usuarios por mail o Uid.
 */
@Dao
interface UsuarioDAO {

    /**
     * Insertar usuario en la base de datos.
     * UsuarioEntity es el objeto a insertar.
     *
     * @param UsuarioEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE) // si ya existe, reemplaza el usuario
    suspend fun insertar(usuario: UsuarioEntity)

    /**
     * Eliminar usuario de la base de datos.
     * UsuarioEntity es el objeto a eliminar.
     *
     * @param UsuarioEntity
     */
    @Delete
    suspend fun eliminarUsuario(usuario: UsuarioEntity)

    /**
     * Eliminar todos los usuarios de la tabla usuarios.
     */
    @Query("DELETE FROM usuarios")
    suspend fun eliminarTodosUsuarios()

    /**
     * Devolver un usuario a partir de email.
     *
     * @param email del usuario.
     * @return usuario o null si no lo encuentra.
     */
    @Query(" SELECT * FROM usuarios WHERE email = :email")
    suspend fun obtenerUsuarioPorEmail(email: String): UsuarioEntity?

    /**
     * Devolver usuario por uid (identificador único).
     * Es de tipo Flow para que puedan ejecutarse cambios en la base de datos en tiempo real.
     *
     * @param uid
     * @return usuario o null si no lo encuentra.
     */
    @Query("SELECT * FROM usuarios WHERE uid = :uid")
    fun obtenerUsuarioPorUid(uid: String): Flow<UsuarioEntity?>

}
