package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.victhor.appvideojuegos.R
import com.victhor.appvideojuegos.navigation.Routes
import com.victhor.appvideojuegos.ui.theme.AcentoNeonBlue
import com.victhor.appvideojuegos.ui.theme.AlertaNeonRojo
import com.victhor.appvideojuegos.ui.theme.FondoPantallaNegro
import com.victhor.appvideojuegos.ui.theme.TextoSecundarioGris
import com.victhor.appvideojuegos.viewmodel.LoginViewModel
import androidx.compose.material3.AlertDialog
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height

@Composable
fun PantallaLogin(
    navController: NavController,
    viewModel: LoginViewModel
) {
    val state by viewModel.uiState.collectAsState() // Obtener estado desde LoginViewModel

    LaunchedEffect(state.loginExitoso, state.registroExitoso) {
        if (state.loginExitoso || state.registroExitoso) { // Observar si loginExitoso para navegar
            navController.navigate(Routes.Principal.route) {
                popUpTo(Routes.Login.route) { inclusive = true } // Evitar volver pantalla atrás
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoPantallaNegro)
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.nombre),
            contentDescription = "Logo app",
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp), // Reducir un poco
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Campos azul neon
        Column(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { viewModel.cambiarEmail(it) },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AcentoNeonBlue,
                    focusedLabelColor = AcentoNeonBlue,
                    unfocusedBorderColor = TextoSecundarioGris.copy(alpha = 0.3f),
                    cursorColor = AcentoNeonBlue
                )
            )
            Spacer(modifier = Modifier.height(48.dp))

            OutlinedTextField(
                value = state.password,
                onValueChange = { viewModel.cambiarPassword(it) },
                label = { Text("Contraseña") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(4.dp),
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AcentoNeonBlue,
                    focusedLabelColor = AcentoNeonBlue,
                    unfocusedBorderColor = TextoSecundarioGris.copy(alpha = 0.3f),
                    cursorColor = AcentoNeonBlue
                )
            )

            // Error
            if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = AlertaNeonRojo,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(48.dp))

        // Botones azul neon
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Iniciar sesion azul neón
            Button(
                onClick = { viewModel.iniciarSesion() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AcentoNeonBlue,
                    contentColor = Color.Black
                ),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = Color.Black
                    )
                } else {
                    Text("Iniciar sesión", fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Registrarse
            Button(
                onClick = { viewModel.registrarUsuario() },
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(48.dp),
                shape = MaterialTheme.shapes.medium,
                enabled = !state.isLoading
            ) {
                Text(
                    text = "REGÍSTRATE AQUÍ",
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }

    // --- DIÁLOGO DE NOMBRE (Pégalo al final de la función PantallaLogin) ---
    if (state.mostrarDialogoNombre) {
        AlertDialog(
            onDismissRequest = { },
            title = { Text("IDENTIDAD_REQUERIDA", color = AcentoNeonBlue, fontWeight = FontWeight.Bold) },
            text = {
                Column {
                    Text("Configura tu nombre de usuario para el sistema:", color = Color.White)
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.nombreUsuario,
                        onValueChange = { viewModel.cambiarNombreUsuario(it) },
                        label = { Text("NICKNAME") },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AcentoNeonBlue)
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Accedemos a Firebase para pillar el UID y terminar el proceso
                        val uid = com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        viewModel.guardarNombreYFinalizar(uid)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AcentoNeonBlue)
                ) {
                    Text("CONFIRMAR", color = Color.Black)
                }
            },
            containerColor = FondoPantallaNegro,
            shape = RoundedCornerShape(8.dp)
        )
    }

}