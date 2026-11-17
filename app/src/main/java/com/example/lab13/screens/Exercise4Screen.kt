package com.example.lab13.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

enum class ScreenState { Cargando, Contenido, Error }

@Composable
fun Exercise4Screen() {
    var state by remember { mutableStateOf(ScreenState.Cargando) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ejercicio 4: AnimatedContent",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        AnimatedContent(
            targetState = state,
            transitionSpec = {
                fadeIn(
                    animationSpec = androidx.compose.animation.core.tween(600)
                ) togetherWith fadeOut(
                    animationSpec = androidx.compose.animation.core.tween(300)
                )
            },
            label = "stateTransition"
        ) { targetState ->
            when (targetState) {
                ScreenState.Cargando -> LoadingContent()
                ScreenState.Contenido -> SuccessContent()
                ScreenState.Error -> ErrorContent()
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = { state = ScreenState.Cargando },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF1565C0)
                )
            ) {
                Text("Cargando")
            }
            Button(
                onClick = { state = ScreenState.Contenido },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2E7D32)
                )
            ) {
                Text("Contenido")
            }
            Button(
                onClick = { state = ScreenState.Error },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFC62828)
                )
            ) {
                Text("Error")
            }
        }
    }
}

@Composable
private fun LoadingContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE3F2FD)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator(color = Color(0xFF1565C0))
            Text(
                text = "Cargando datos...",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFF1565C0)
            )
        }
    }
}

@Composable
private fun SuccessContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFE8F5E9)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF2E7D32)),
                contentAlignment = Alignment.Center
            ) {
                Text("✓", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
            Text(
                text = "¡Contenido cargado exitosamente!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFF2E7D32)
            )
            Text(
                text = "Los datos se muestran correctamente.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFF388E3C),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ErrorContent() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFFFFEBEE)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFC62828)),
                contentAlignment = Alignment.Center
            ) {
                Text("✗", color = Color.White, style = MaterialTheme.typography.headlineMedium)
            }
            Text(
                text = "¡Error al cargar!",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 16.dp),
                color = Color(0xFFC62828)
            )
            Text(
                text = "Ocurrió un problema. Intenta de nuevo.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 8.dp),
                color = Color(0xFFD32F2F),
                textAlign = TextAlign.Center
            )
        }
    }
}
