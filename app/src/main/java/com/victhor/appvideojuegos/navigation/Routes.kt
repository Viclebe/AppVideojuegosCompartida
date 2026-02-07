package com.victhor.appvideojuegos.navigation

sealed class Routes(val route: String) {
    object Splash : Routes("splash")
    object Onboarding : Routes("onboarding")
    object Principal : Routes("principal")
    object Insertar : Routes("insertar")
    object Modificar : Routes("modificar")
    object Detalle : Routes("detalle")
    object Estadisticas : Routes("estadisticas")
    object Buscar : Routes("filtros")
    object Ajustes : Routes("ajustes")
}
