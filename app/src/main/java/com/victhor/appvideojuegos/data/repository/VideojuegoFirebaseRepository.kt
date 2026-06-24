package com.victhor.appvideojuegos.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.victhor.appvideojuegos.domain.model.Comentario
import com.victhor.appvideojuegos.domain.model.Videojuego
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class VideojuegoFirebaseRepository {

    // Conectar a la colección principal
    private val firestore = FirebaseFirestore.getInstance()
    private val coleccion = firestore.collection("videojuegos")

    /**
     * Obtener y escuchar los videojuegos con Firestore.
     *
     */
    fun listarVideojuegos(usuarioId: String): Flow<List<Videojuego>> = callbackFlow {
        // Buscamos solo los juegos de este usuario
        val listener = coleccion.whereEqualTo("usuarioId", usuarioId)

            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                // Mapeamos los documentos y les inyectamos su ID de Firestore en el campo puente
                val listaJuegos = snapshot?.documents?.mapNotNull { documento ->
                    documento.toObject(Videojuego::class.java)?.copy(firestoreId = documento.id)
                } ?: emptyList()

                trySend(listaJuegos) // Emitimos la lista a la UI
            }

        // Si se cierra la pantalla, matamos el listener para no consumir batería/datos
        awaitClose { listener.remove() }
    }

    /**
     * Insertar un videojuego.
     */
    suspend fun insertarVideojuego(videojuego: Videojuego) {
        // .document() sin parámetros crea un ID alfanumérico único para Firestore
        val nuevoDocumento = coleccion.document()

        // Pero opcionalmente podemos guardar nuestro propio ID (si tú se lo asignabas manualmente en Room)
        // val nuevoDocumento = coleccion.document(videojuego.id.toString())

        nuevoDocumento.set(videojuego.copy(firestoreId = nuevoDocumento.id))
            .await() // Guarda el objeto entero
    }

    /**
     * Modificar un videojuego (ID de Firestore)
     */
    suspend fun modificarVideojuego(idFirestore: String, videojuego: Videojuego) {
        coleccion.document(idFirestore).set(videojuego).await()
    }

    /**
     * Buscar por ID en Firestore
     */
    fun buscarPorId(id: String): Flow<Videojuego?> = callbackFlow {
        // Escuchador en tiempo real sobre ese documento (cambios)
        // Coleccion de Firestore (usuarios) Strings
        val listener = coleccion.document(id).addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error); return@addSnapshotListener
            }
            // Mapear documento único y inyectar su ID
            val juego = snapshot?.toObject(Videojuego::class.java)?.copy(firestoreId = snapshot.id)
            trySend(juego)
        }
        awaitClose { // Detener escucha
            listener.remove()
        }
    }

    /**
     * Función para añadir un like por usuario
     * Si el usuario no tiene like lo añade, si ya lo tiene lo quita.
     */
    suspend fun darLike(idFirestore: String, usuarioId: String) {
        val docRef = coleccion.document(idFirestore)
        val snapshot = docRef.get().await()
        val likesActuales = snapshot.get("likes") as? List<*> ?: emptyList<Any>()
        
        if (likesActuales.contains(usuarioId)) {
            docRef.update("likes", FieldValue.arrayRemove(usuarioId)).await()
        } else {
            docRef.update("likes", FieldValue.arrayUnion(usuarioId)).await()
        }
    }

    /**
     * Eliminar un único videojuego de Firebase
     */
    suspend fun eliminarVideojuego(idFirestore: String) {
        coleccion.document(idFirestore).delete().await()
    }

    /**
     * Nuevo método para borrar todos los juegos de un usuario
     *      */
    suspend fun eliminarTodaBiblioteca(usuarioId: String) {
        val juegos = coleccion.whereEqualTo("usuarioId", usuarioId).get().await()
        for (documento in juegos) {
            documento.reference.delete().await()
        }
    }

    /**
     * Mostrar los juegos de toda la comunidad ordenados por fecha.
     * PARA COMUNIDADVIEWMODEL
     */
    fun listarTotalComunidad(): Flow<List<Videojuego>> = callbackFlow {
        // Apuntar a coleción ordenar por fecha
        val query = coleccion.orderBy(
            "fechaCreacionModificacion",
            com.google.firebase.firestore.Query.Direction.DESCENDING
        )

        // Listener
        val listener = query.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            // Convertir documentos en Videojuegos
            val videojuegos = snapshot?.documents?.mapNotNull { documentos ->
                documentos.toObject(Videojuego::class.java)?.copy(firestoreId = documentos.id)
            } ?: emptyList()
            trySend(videojuegos)
        }
        awaitClose { listener.remove() } // Finalizar escucha
    }

    /**
     * Guardar un comentario en Firebase.
    */
    suspend fun guardarComentario(comentario: Comentario) {
        val nuevoDoc = firestore.collection("comentarios").document()
        nuevoDoc.set(comentario.copy(firestoreId = nuevoDoc.id)).await()
    }

    /**
     * Obtener comentarios de un videojuego.
     */
    fun obtenerComentarios(firestoreIdVideojuego: String): Flow<List<Comentario>> = callbackFlow {
        val listener = firestore.collection("comentarios")
            .whereEqualTo("firestoreIdVideojuego", firestoreIdVideojuego)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(emptyList())
                    return@addSnapshotListener
                }
                val lista = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(Comentario::class.java)?.copy(firestoreId = doc.id)
                } ?: emptyList()
                
                // Ordenar la lista
                val listaOrdenada = lista.sortedByDescending { it.fechaComentario }
                trySend(listaOrdenada)
            }
        awaitClose { listener.remove() }
    }
}
