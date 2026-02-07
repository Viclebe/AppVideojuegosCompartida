package com.victhor.appvideojuegos.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.victhor.appvideojuegos.R

@Composable
fun AuthorInfo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.nombre),
            contentDescription = "Logo app",
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .padding(8.dp),
            contentScale = ContentScale.Fit
        )
    }
}
