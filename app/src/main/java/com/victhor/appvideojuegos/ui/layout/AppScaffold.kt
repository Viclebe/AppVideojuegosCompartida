package com.victhor.appvideojuegos.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppScaffold(
    showBackArrow: Boolean = false,
    onBackArrowClick: () -> Unit = {},
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = {
            AppTopBar(
                showBackArrow = showBackArrow,
                onClickBackArrow = onBackArrowClick
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Box(
                modifier = Modifier
                    .weight(9f)
                    .fillMaxWidth()
            ) {
                content()
            }

            HorizontalDivider(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.onPrimary)
                    .height(2.dp)
            )

            AuthorInfo(
                modifier = Modifier
                    .padding(vertical = 4.dp)
                    .weight(1f)
            )
        }
    }
}
