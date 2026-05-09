package com.victhor.appvideojuegos.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victhor.appvideojuegos.data.local.database.VideojuegoDatabase
import com.victhor.appvideojuegos.data.repository.ComentarioRepository
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.data.repository.ValoracionRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository
import com.victhor.appvideojuegos.data.repository.UsuarioVideojuegoRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoFirebaseRepository

class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Obtenemos la instancia de la base de datos y los DAO
        val db = VideojuegoDatabase.obtenerInstancia(application)
        val videojuegoDao = db.videojuegoDao()
        val usuarioDao = db.usuarioDao()
        val comentarioDao = db.comentarioDao()
        val valoracionDao = db.valoracionDao()
        val usuarioVideojuegoDao = db.usuarioVideojuegoDao()

        val repository = VideojuegoRepository(videojuegoDao, usuarioVideojuegoDao)
        val usuarioRepository = UsuarioRepository(usuarioDao)
        val comentarioRepository = ComentarioRepository(comentarioDao)
        val valoracionRepository = ValoracionRepository(valoracionDao)
        val usuarioVideojuegoRepository = UsuarioVideojuegoRepository(usuarioVideojuegoDao)

        // --- REPOSITORIOS FIREBASE ---
        val firebaseVideojuegoRepository = VideojuegoFirebaseRepository()

        return when {
            modelClass.isAssignableFrom(PrincipalViewModel::class.java) -> {
                PrincipalViewModel(firebaseVideojuegoRepository) as T
            }

            modelClass.isAssignableFrom(ComunidadViewModel::class.java) -> {
                ComunidadViewModel(firebaseVideojuegoRepository) as T
            }

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(usuarioRepository) as T
            }

            modelClass.isAssignableFrom(PerfilViewModel::class.java) -> {
                PerfilViewModel(usuarioRepository) as T
            }

            modelClass.isAssignableFrom(InsertarViewModel::class.java) -> {
                InsertarViewModel(
                    repository,
                    usuarioVideojuegoRepository,
                    firebaseVideojuegoRepository
                ) as T
            }

            modelClass.isAssignableFrom(AjustesViewModel::class.java) -> {
                AjustesViewModel(firebaseVideojuegoRepository) as T
            }

            modelClass.isAssignableFrom(ModificarViewModel::class.java) -> {
                ModificarViewModel(
                    repository,
                    usuarioVideojuegoRepository,
                    firebaseVideojuegoRepository
                ) as T
            }

            modelClass.isAssignableFrom(EstadisticasViewModel::class.java) -> {
                EstadisticasViewModel(repository, usuarioVideojuegoRepository) as T
            }

            modelClass.isAssignableFrom(DetalleViewModel::class.java) -> {
                DetalleViewModel(
                    repository,
                    comentarioRepository,
                    usuarioRepository,
                    valoracionRepository,
                    usuarioVideojuegoRepository,
                    firebaseVideojuegoRepository
                ) as T
            }

            modelClass.isAssignableFrom(BuscarViewModel::class.java) -> {
                BuscarViewModel(repository) as T
            }

            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}