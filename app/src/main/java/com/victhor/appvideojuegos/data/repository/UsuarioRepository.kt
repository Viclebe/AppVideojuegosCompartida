package com.victhor.appvideojuegos.data.repository

import com.victhor.appvideojuegos.data.local.dao.UsuarioDAO
import com.victhor.appvideojuegos.data.local.entity.UsuarioEntity
import com.victhor.appvideojuegos.domain.model.Usuario
import com.victhor.appvideojuegos.sesion.Sesion
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * UsuarioRepositorio: clase intermediaria entre BBDD DAO y UI.
 * Recoge los datos y convierte las entidades en objetos (modelos de dominio).
 *
 * Las funciones suspend porque realiza una operación de base de datos que puede tardar
 * y debe ejecutarse dentro de una coroutine (viewModelScope.launch) para no bloquear la UI (hilos).
 */
class UsuarioRepository(private val dao: UsuarioDAO) {

    /**
     * Insertar usuario, convierte el objeto Usuario en una Entity.
     *
     * @param usuario insertado.
     */
    suspend fun insertarUsuario(usuario: Usuario) {
        dao.insertar(
            UsuarioEntity(
                uid = usuario.uid,
                nombre = usuario.nombre,
                email = usuario.email,
                fechaRegistro = usuario.fechaRegistro,
                avatarUrl = usuario.avatarUrl
            )
        )

    }

    /**
     * Obtener usuario por el uid pasado como parámetro.
     * Convierte videojuegoEntity en un objeto Videojuego tipo Flow.
     *
     * @param uid del usuario.
     * @return Usuario encontrado.
     */
    fun obtenerUsuario(uid: String): Flow<Usuario?> {
        return dao.obtenerUsuarioPorUid(uid)
            .map { entity -> // Transforma la lista de entidades y convertir Entity en objeto
                entity?.let { // Si entity no devuelve null, convertir. Si es null, devuelve null
                    Usuario(
                        uid = it.uid,
                        nombre = it.nombre,
                        email = it.email,
                        fechaRegistro = it.fechaRegistro,
                        avatarUrl = it.avatarUrl
                    )
                }
            }
    }

    /**
     * Comprobar si el correo existe (Para el Login simulado)
     */
    suspend fun obtenerUsuarioPorEmail(email: String): Usuario? {
        val entity = dao.obtenerUsuarioPorEmail(email)
        return if (entity != null) {
            Usuario(
                uid = entity.uid,
                nombre = entity.nombre,
                email = entity.email,
                fechaRegistro = entity.fechaRegistro,
                avatarUrl = entity.avatarUrl
            )
        } else {
            null
        }
    }
}