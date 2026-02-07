package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.navigation.Routes


@Composable
fun PantallaOnboarding(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        //logo
        Image(
            painter = painterResource(id = R.drawable.logonombre),
            contentDescription = "logo_app",
            modifier = Modifier.size(500.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text("Convierte cada partida en memoria: juega, guarda, recuerda")
        Button(onClick = {
            navController.navigate(Routes.Principal.route)
        }) {
            Text("Comenzar")
        }
        Spacer(modifier = Modifier.height(40.dp))

    }

}