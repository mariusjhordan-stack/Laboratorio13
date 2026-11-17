package com.example.lab13.screens

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun Exercise2Screen() {
    var isShielded by remember { mutableStateOf(true) }
    var useSpring by remember { mutableStateOf(false) }
    var activations by remember { mutableIntStateOf(0) }

    val shieldColor = Color(0xFF448AFF)
    val overchargeColor = Color(0xFF00E676)

    val animatedColor by animateColorAsState(
        targetValue = if (isShielded) shieldColor else overchargeColor,
        animationSpec = if (useSpring) {
            spring(stiffness = Spring.StiffnessLow)
        } else {
            tween(durationMillis = 1000)
        },
        label = "shieldColor"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A2E))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "🔋 Activaciones: $activations",
                color = Color(0xFFFFD54F),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "ESCUDO DE ENERGÍA",
                color = Color(0xFF448AFF),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "animateColorAsState con selector tween / spring",
                color = Color(0xFF8888AA),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val center = Offset(size.width / 2, size.height / 2)
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                animatedColor.copy(alpha = 0.6f),
                                animatedColor.copy(alpha = 0.2f),
                                Color.Transparent
                            ),
                            center = center,
                            radius = size.minDimension / 2
                        ),
                        radius = size.minDimension / 2,
                        center = center
                    )
                    drawCircle(
                        color = animatedColor.copy(alpha = 0.4f),
                        radius = size.minDimension / 2 * 0.75f,
                        center = center
                    )
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                animatedColor,
                                animatedColor.copy(alpha = 0.7f)
                            ),
                            center = center,
                            radius = size.minDimension / 2 * 0.45f
                        ),
                        radius = size.minDimension / 2 * 0.45f,
                        center = center
                    )
                }

                Text("🚀", fontSize = 48.sp)
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = if (isShielded) "🛡️ Escudo Activo" else "⚡ Sobrecarga",
                color = animatedColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    isShielded = !isShielded
                    activations++
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isShielded) overchargeColor else shieldColor
                ),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(
                    text = if (isShielded) "Sobrecargar ⚡" else "Restaurar 🛡️",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1A2E)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tipo de animación:",
                color = Color(0xFFCCCCDD),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                FilterChip(
                    selected = !useSpring,
                    onClick = { useSpring = false },
                    label = { Text("Tween (1s)", color = Color.White) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF448AFF),
                        containerColor = Color(0xFF2A2A4A)
                    )
                )
                FilterChip(
                    selected = useSpring,
                    onClick = { useSpring = true },
                    label = { Text("Spring", color = Color.White) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFF448AFF),
                        containerColor = Color(0xFF2A2A4A)
                    )
                )
            }
        }
    }
}
