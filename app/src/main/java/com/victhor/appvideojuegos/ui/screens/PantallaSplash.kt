package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.victhor.appvideojuegos.navigation.Routes
import kotlinx.coroutines.delay
import com.victhor.appvideojuegos.R
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme


@Composable
fun PantallaSplash(navController: NavController) {
    //Efecto que se ejecuta una sola vez
    LaunchedEffect(Unit) {
        delay(2000)
        navController.navigate(Routes.Login.route) {
            popUpTo(Routes.Splash.route) {
                inclusive = true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.splash),
            contentDescription = "logo_app",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
    }
}