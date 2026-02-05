package com.victhor.appvideojuegos.ui.layout

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.victhor.appvideojuegos.R

@Composable
fun AuthorInfo(modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .then(modifier),
        horizontalArrangement = Arrangement.spacedBy(
            20.dp,
            Alignment.CenterHorizontally
        ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "logo_app",
            modifier = Modifier
                .clip(CircleShape)
                .border(
                    width = 3.dp,
                    color = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape
                )
                .width(60.dp)
        )
        Text(
            text = "Autor Víctor León",
            fontSize = 20.sp,
            fontWeight = androidx.compose.ui.text.font.FontWeight.Bold,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}
