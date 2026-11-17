package com.example.lab13.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

enum class MissionState { Cargando, Contenido, Error }

@Composable
fun Exercise4Screen() {
    var state by remember { mutableStateOf(MissionState.Cargando) }

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
                text = "PANEL DE MISIÓN",
                color = Color(0xFF66BB6A),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "AnimatedContent con fadeIn/fadeOut personalizados",
                color = Color(0xFF8888AA),
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            AnimatedContent(
                targetState = state,
                transitionSpec = {
                    fadeIn(tween(600)) togetherWith fadeOut(tween(300))
                },
                label = "missionTransition"
            ) { targetState ->
                when (targetState) {
                    MissionState.Cargando -> MissionLoading()
                    MissionState.Contenido -> MissionComplete()
                    MissionState.Error -> MissionError()
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { state = MissionState.Cargando },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF448AFF)
                    )
                ) {
                    Text("Cargando", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { state = MissionState.Contenido },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF66BB6A)
                    )
                ) {
                    Text("Contenido", fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = { state = MissionState.Error },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFEF5350)
                    )
                ) {
                    Text("Error", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun MissionLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0D2137)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🛸", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(16.dp))
            CircularProgressIndicator(
                color = Color(0xFF448AFF),
                strokeWidth = 3.dp,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "BUSCANDO PARTIDA...",
                color = Color(0xFF448AFF),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Text(
                text = "Escaneando sector galáctico",
                color = Color(0xFF6688BB),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Composable
private fun MissionComplete() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF0D2D14)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🏆", fontSize = 48.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "¡NIVEL COMPLETADO!",
                color = Color(0xFF66BB6A),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFF1B5E20))
                    .padding(horizontal = 20.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "+100 PTS",
                    color = Color(0xFFFFD54F),
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                )
            }
            Text(
                text = "Todos los objetivos cumplidos",
                color = Color(0xFF81C784),
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }
}

@Composable
private fun MissionError() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color(0xFF2D0D0D)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🛑", fontSize = 40.sp)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "¡ESCUDOS CAÍDOS!",
                color = Color(0xFFEF5350),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Error de Sistema",
                color = Color(0xFFEF9A9A),
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF4A1111))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = "⚠ Conexión perdida con la base",
                    color = Color(0xFFFF8A80),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
