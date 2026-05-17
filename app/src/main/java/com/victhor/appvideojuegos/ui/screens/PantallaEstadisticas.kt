package com.victhor.appvideojuegos.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.victhor.appvideojuegos.ui.layout.AppScaffold
import com.victhor.appvideojuegos.viewmodel.EstadisticasViewModel
import androidx.compose.foundation.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.victhor.appvideojuegos.ui.theme.*


@Composable
fun PantallaEstadisticas(
    navController: NavController,
    viewModel: EstadisticasViewModel
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarEstadisticas()
    }

    AppScaffold {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(FondoPantallaNegro)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = null, tint = AcentoNeonCyan)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Estadísticas",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.ExtraBold,
                    color = AcentoNeonCyan,
                    letterSpacing = 2.sp
                )
            }
            Spacer(modifier = Modifier.size(25.dp))

            //CARDS
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CajaTotal(
                    titulo = "JUEGOS",
                    valor = uiState.total.toString(),
                    color = AcentoNeonCyan,
                    modifier = Modifier.weight(1f)
                )
                CajaTotal(
                    titulo = "MEDIA VALORACIONES",
                    valor = uiState.mediaValoracion.toString(),
                    color = AcentoNeonMagenta,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            BarraEstadistica("JUGANDO", uiState.jugando, uiState.total, EstadoNeonAmarillo)
            BarraEstadistica("FINALIZADOS", uiState.finalizados, uiState.total, EstadoNeonVerde)
            BarraEstadistica("PENDIENTES", uiState.pendientes, uiState.total, AcentoNeonBlue)

            Spacer(modifier = Modifier.height(32.dp))

            // --- 3. PLATAFORMAS (Lista con iconos) ---
            Text(text = "PLATAFORMAS", color = TextoPrincipalBlanco, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))

            uiState.juegosPorPlataforma.forEach { (plataforma, cantidad) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .background(FondoContenedoresOscuro, RoundedCornerShape(4.dp))
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        plataforma.uppercase(),
                        color = AcentoNeonCyan,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        cantidad.toString(),
                        color = TextoPrincipalBlanco,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CajaTotal(titulo: String, valor: String, color: Color, modifier: Modifier) {
    Surface(
        modifier = modifier,
        color = FondoContenedoresOscuro,
        border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(titulo, color = color, style = MaterialTheme.typography.labelSmall)
            Text(
                valor,
                color = TextoPrincipalBlanco,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun BarraEstadistica(label: String, valor: Int, total: Int, color: Color) {
    val progreso = if (total > 0) valor.toFloat() / total else 0f

    Column(modifier = Modifier.padding(bottom = 16.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, color = TextoSecundarioGris, style = MaterialTheme.typography.labelSmall)
            Text("$valor", color = color, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = progreso,
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = color,
            trackColor = FondoContenedoresOscuro
        )
    }
}
