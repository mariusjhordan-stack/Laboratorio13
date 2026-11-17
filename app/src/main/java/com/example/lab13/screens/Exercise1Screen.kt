package com.example.lab13.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun Exercise1Screen() {
    var chestOpen by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var lastReward by remember { mutableStateOf("") }
    var lastWasEnemy by remember { mutableStateOf(false) }

    val rewards = listOf(
        "💎 Diamante +50" to 50,
        "🪙 Moneda de Oro +20" to 20,
        "⚔️ Espada Legendaria +30" to 30,
        "🛡️ Escudo Sagrado +25" to 25,
        "🧪 Poción Mágica +15" to 15
    )
    val enemies = listOf(
        "💀 Esqueleto -10" to -10,
        "🕷️ Araña Venenosa -15" to -15,
        "👻 Fantasma -5" to -5
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
                text = "⭐ $score pts",
                color = Color(0xFFFFD54F),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "COFRE DEL TESORO",
                color = Color(0xFFFF6E40),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "AnimatedVisibility: fadeIn + expandVertically / fadeOut + shrinkVertically",
                color = Color(0xFF8888AA),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = if (chestOpen) "📭" else "📦",
                fontSize = 80.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            AnimatedVisibility(
                visible = chestOpen,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(
                            if (lastWasEnemy) Color(0xFF3D1111) else Color(0xFF112211)
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = lastReward,
                            color = if (lastWasEnemy) Color(0xFFFF6E6E) else Color(0xFF64FFDA),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (lastWasEnemy) "¡Emboscada!" else "¡Recompensa!",
                            color = if (lastWasEnemy) Color(0xFFFF8A80) else Color(0xFFA5D6A7),
                            fontSize = 14.sp,
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (!chestOpen) {
                        val isEnemy = Random.nextFloat() < 0.3f
                        if (isEnemy) {
                            val (name, pts) = enemies.random()
                            lastReward = name
                            score = (score + pts).coerceAtLeast(0)
                            lastWasEnemy = true
                        } else {
                            val (name, pts) = rewards.random()
                            lastReward = name
                            score += pts
                            lastWasEnemy = false
                        }
                        chestOpen = true
                    } else {
                        chestOpen = false
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (chestOpen) Color(0xFF455A64) else Color(0xFFFF6E40)
                ),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(
                    text = if (chestOpen) "Cerrar Cofre" else "¡Abrir Cofre!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Abre cofres para ganar tesoros o enfrentar enemigos",
                    color = Color(0xFF6666AA),
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
