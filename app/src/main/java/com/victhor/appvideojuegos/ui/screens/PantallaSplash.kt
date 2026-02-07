package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.navigation.Routes
import kotlinx.coroutines.delay
import com.victhor.appvideojuegos.R


@Composable
fun PantallaSplash(navController: NavController) {
    //Efecto que se ejecuta una sola vez
    LaunchedEffect(Unit) {
        delay(5000)
        navController.navigate(Routes.Onboarding.route) {
            popUpTo(Routes.Splash.route) {
                inclusive = true
            }
        }
    }

    //Logo + texto
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.logonombre),
            contentDescription = "logo_app",
            modifier = Modifier.size(500.dp)
        )
    }
}