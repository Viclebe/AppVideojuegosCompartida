package com.victhor.appvideojuegos.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.victhor.appvideojuegos.data.local.database.VideojuegoDatabase
import com.victhor.appvideojuegos.data.repository.UsuarioRepository
import com.victhor.appvideojuegos.data.repository.VideojuegoRepository

class AppViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // Obtenemos la instancia de la base de datos y los DAO
        val db = VideojuegoDatabase.obtenerInstancia(application)
        val dao = db.videojuegoDao()
        val usuarioDao = db.usuarioDao()
        
        val repository = VideojuegoRepository(dao)
        val usuarioRepository = UsuarioRepository(usuarioDao)
        
        return when {
            modelClass.isAssignableFrom(PrincipalViewModel::class.java) -> {
                PrincipalViewModel(repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(usuarioRepository) as T
            }
            modelClass.isAssignableFrom(PerfilViewModel::class.java) -> {
                PerfilViewModel(usuarioRepository) as T
            }
            // Los AndroidViewModels se pueden instanciar así (o dejarlos con el viewModel() por defecto):
            modelClass.isAssignableFrom(InsertarViewModel::class.java) -> {
                InsertarViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AjustesViewModel::class.java) -> {
                AjustesViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ModificarViewModel::class.java) -> {
                ModificarViewModel(repository) as T
            }
            modelClass.isAssignableFrom(EstadisticasViewModel::class.java) -> {
                EstadisticasViewModel(repository) as T
            }
            modelClass.isAssignableFrom(DetalleViewModel::class.java) -> {
                DetalleViewModel(repository) as T
            }
            modelClass.isAssignableFrom(BuscarViewModel::class.java) -> {
                BuscarViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
        }
    }
}