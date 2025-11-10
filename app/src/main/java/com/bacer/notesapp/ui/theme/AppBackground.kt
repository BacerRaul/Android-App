package com.bacer.notesapp.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.bacer.notesapp.R // Generated automatically


@Composable
fun SubjectGradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            // Top
                            Color(0xFF001C3D),
                            Color(0xFF003566),
                            Color(0xFF005F99),
                            Color(0xFF005F99),
                            Color(0xFF003566),
                            Color(0xFF001C3D)
                            // Bottom
                        )
                    )
                )
        )
        // ----- Gradient

        // Image
        Image(
            painter = painterResource(id = R.drawable.lines),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.12f // Transparency
        )
        // ----- Image

        content()
    }
}

@Composable
fun GradeGradientBackground(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Gradient
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            // Top
                            Color(0xFF28003D),
                            Color(0xFF4A007A),
                            Color(0xFF6F00B3),
                            Color(0xFF6F00B3),
                            Color(0xFF4A007A),
                            Color(0xFF28003D)
                            // Bottom
                        )
                    )
                )
        )
        // ----- Gradient

        // Image
        Image(
            painter = painterResource(id = R.drawable.lines),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.12f // Transparency
        )
        // ----- Image

        content()
    }
}

