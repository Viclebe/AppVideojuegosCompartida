package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.navigation.Routes


@Composable
fun PantallaOnboarding(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // logo
        Image(
            painter = painterResource(id = R.drawable.logonombre),
            contentDescription = "Logo app",
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp),
            contentScale = ContentScale.Fit
        )

        // Texto
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Juega · Guarda · Recuerda",
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Convierte cada partida en memoria",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.75f)
            )
        }

        // Botón a pantalla principal
        Button(
            onClick = { navController.navigate(Routes.Principal.route) },
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(48.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = "Empezar",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
