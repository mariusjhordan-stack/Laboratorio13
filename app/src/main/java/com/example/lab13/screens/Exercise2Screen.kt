package com.example.lab13.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.unit.dp

@Composable
fun Exercise2Screen() {
    var isBlue by remember { mutableStateOf(true) }
    var useSpring by remember { mutableStateOf(false) }

    val targetColor = if (isBlue) Color(0xFF1565C0) else Color(0xFF2E7D32)

    val animatedColor by animateColorAsState(
        targetValue = targetColor,
        animationSpec = if (useSpring) {
            spring(stiffness = Spring.StiffnessLow)
        } else {
            tween(durationMillis = 1000)
        },
        label = "colorAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ejercicio 2: animateColorAsState",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Box(
            modifier = Modifier
                .size(180.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(animatedColor)
        )

        Text(
            text = if (isBlue) "Azul" else "Verde",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(top = 16.dp)
        )

        Button(
            onClick = { isBlue = !isBlue },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Cambiar Color")
        }

        Text(
            text = "Tipo de animación:",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = !useSpring,
                onClick = { useSpring = false },
                label = { Text("Tween (1s)") }
            )
            FilterChip(
                selected = useSpring,
                onClick = { useSpring = true },
                label = { Text("Spring") }
            )
        }
    }
}
