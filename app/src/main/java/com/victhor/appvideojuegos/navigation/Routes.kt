package com.victhor.appvideojuegos.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Onboarding : Routes("onboarding")

    object Principal : Routes("principal")
    object Insertar : Routes("insertar")

    object Modificar : Routes("modificar") {
        //fun rutaModificar(id: Int) = "modificar/$id"
    }

    object Detalle : Routes("detalle") {
        //fun rutaDetalle(id: Int) = "detalle/$id"
    }

    object Estadisticas : Routes("estadisticas")
    object Buscar : Routes("filtros")
    object Ajustes : Routes("ajustes")
}
