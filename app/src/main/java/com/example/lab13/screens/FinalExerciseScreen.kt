package com.example.lab13.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.roundToInt
import kotlin.random.Random

enum class GameState { Menu, Playing, GameOver }

data class Obstacle(val x: Float, val y: Float, val id: Int, val isEnergy: Boolean = false)

@Composable
fun FinalExerciseScreen() {
    var gameState by remember { mutableStateOf(GameState.Menu) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var shipX by remember { mutableFloatStateOf(180f) }
    val obstacles = remember { mutableStateListOf<Obstacle>() }
    var obstacleIdCounter by remember { mutableIntStateOf(0) }
    var showDamageFlash by remember { mutableStateOf(false) }
    var showEnergyCollect by remember { mutableStateOf(false) }
    var frameCount by remember { mutableIntStateOf(0) }

    // AnimatedContent: transitions between Menu, Playing, GameOver
    AnimatedContent(
        targetState = gameState,
        transitionSpec = {
            fadeIn(tween(400)) togetherWith fadeOut(tween(200))
        },
        label = "gameStateTransition"
    ) { state ->
        when (state) {
            GameState.Menu -> MenuScreen(
                onStart = {
                    score = 0
                    lives = 3
                    shipX = 180f
                    obstacles.clear()
                    obstacleIdCounter = 0
                    frameCount = 0
                    gameState = GameState.Playing
                }
            )
            GameState.Playing -> {
                // animateColorAsState: flash red on damage, green on energy pickup
                val bgColor by animateColorAsState(
                    targetValue = when {
                        showDamageFlash -> Color(0x33FF0000)
                        showEnergyCollect -> Color(0x3300FF00)
                        else -> Color(0xFF0D1B2A)
                    },
                    animationSpec = tween(300),
                    label = "bgFlash"
                )

                // animateDpAsState: ship size pulses when collecting energy
                val shipSize by animateDpAsState(
                    targetValue = if (showEnergyCollect) 52.dp else 44.dp,
                    animationSpec = spring(),
                    label = "shipSize"
                )

                LaunchedEffect(Unit) {
                    while (gameState == GameState.Playing) {
                        delay(50L)
                        frameCount++

                        // Spawn obstacles every 15 frames
                        if (frameCount % 15 == 0) {
                            val isEnergy = Random.nextFloat() < 0.25f
                            obstacles.add(
                                Obstacle(
                                    x = Random.nextFloat() * 340f,
                                    y = -30f,
                                    id = obstacleIdCounter++,
                                    isEnergy = isEnergy
                                )
                            )
                        }

                        // Move obstacles down
                        val updated = obstacles.map { it.copy(y = it.y + 6f) }
                        obstacles.clear()
                        obstacles.addAll(updated)

                        // Check collisions
                        val shipLeft = shipX - 22f
                        val shipRight = shipX + 22f
                        val shipTop = 520f

                        val toRemove = mutableListOf<Int>()
                        for (obs in obstacles) {
                            if (obs.y > shipTop - 20f && obs.y < shipTop + 44f &&
                                abs(obs.x - shipX) < 40f
                            ) {
                                toRemove.add(obs.id)
                                if (obs.isEnergy) {
                                    score += 10
                                    showEnergyCollect = true
                                } else {
                                    lives--
                                    showDamageFlash = true
                                }
                            }
                        }
                        obstacles.removeAll { it.id in toRemove }

                        // Remove off-screen
                        obstacles.removeAll { it.y > 650f }

                        // Score for surviving
                        if (frameCount % 20 == 0) score++

                        // Reset flashes
                        if (showDamageFlash) {
                            delay(200)
                            showDamageFlash = false
                        }
                        if (showEnergyCollect) {
                            delay(200)
                            showEnergyCollect = false
                        }

                        if (lives <= 0) {
                            gameState = GameState.GameOver
                        }
                    }
                }

                GamePlayScreen(
                    bgColor = bgColor,
                    shipX = shipX,
                    shipSize = shipSize,
                    obstacles = obstacles,
                    score = score,
                    lives = lives,
                    showDamageFlash = showDamageFlash,
                    onDrag = { delta ->
                        shipX = (shipX + delta).coerceIn(22f, 358f)
                    }
                )
            }
            GameState.GameOver -> GameOverScreen(
                score = score,
                onRestart = {
                    score = 0
                    lives = 3
                    shipX = 180f
                    obstacles.clear()
                    obstacleIdCounter = 0
                    frameCount = 0
                    gameState = GameState.Playing
                },
                onMenu = { gameState = GameState.Menu }
            )
        }
    }
}

@Composable
private fun MenuScreen(onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                "🚀",
                fontSize = 64.sp
            )
            Text(
                text = "ESCAPE ESPACIAL",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF64FFDA),
                modifier = Modifier.padding(top = 16.dp)
            )
            Text(
                text = "Esquiva meteoritos ☄️\nAtrapa energía ⚡",
                style = MaterialTheme.typography.bodyLarge,
                color = Color(0xFFB0BEC5),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
            )
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF64FFDA)
                )
            ) {
                Text("JUGAR", color = Color(0xFF0D1B2A), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun GamePlayScreen(
    bgColor: Color,
    shipX: Float,
    shipSize: androidx.compose.ui.unit.Dp,
    obstacles: List<Obstacle>,
    score: Int,
    lives: Int,
    showDamageFlash: Boolean,
    onDrag: (Float) -> Unit
) {
    val density = LocalDensity.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    onDrag(dragAmount.x)
                }
            }
    ) {
        // HUD
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "⭐ $score",
                color = Color(0xFFFFD54F),
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "❤️".repeat(lives),
                fontSize = 20.sp
            )
        }

        // Obstacles (animateDpAsState + offset for movement)
        obstacles.forEach { obs ->
            val obsOffsetY by animateDpAsState(
                targetValue = with(density) { obs.y.toDp() },
                animationSpec = tween(50),
                label = "obsY${obs.id}"
            )
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            obs.x.roundToInt(),
                            obsOffsetY.roundToPx()
                        )
                    }
                    .size(if (obs.isEnergy) 28.dp else 24.dp)
                    .clip(if (obs.isEnergy) CircleShape else RoundedCornerShape(4.dp))
                    .background(if (obs.isEnergy) Color(0xFF64FFDA) else Color(0xFFFF6E40))
            )
        }

        // Ship
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (shipX - with(density) { shipSize.toPx() / 2 }).roundToInt(),
                        with(density) { 520.dp.roundToPx() }
                    )
                }
                .size(shipSize)
                .clip(CircleShape)
                .background(Color(0xFF64FFDA)),
            contentAlignment = Alignment.Center
        ) {
            Text("🚀", fontSize = 24.sp)
        }

        // AnimatedVisibility: damage warning
        AnimatedVisibility(
            visible = showDamageFlash,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier.align(Alignment.TopCenter).padding(top = 60.dp)
        ) {
            Text(
                text = "¡DAÑO!",
                color = Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp,
                modifier = Modifier
                    .background(
                        Color(0x88000000),
                        RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun GameOverScreen(score: Int, onRestart: () -> Unit, onMenu: () -> Unit) {
    // AnimatedVisibility for the game over panel appearing
    var showPanel by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { showPanel = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)),
        contentAlignment = Alignment.Center
    ) {
        AnimatedVisibility(
            visible = showPanel,
            enter = fadeIn(tween(600)) + expandVertically(tween(400)),
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💥", fontSize = 64.sp)
                Text(
                    text = "GAME OVER",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFF6E40),
                    modifier = Modifier.padding(top = 16.dp)
                )
                Text(
                    text = "Puntaje: $score",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color(0xFFFFD54F),
                    modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
                )
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF64FFDA)
                    ),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text("REINTENTAR", color = Color(0xFF0D1B2A), fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onMenu,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF455A64)
                    )
                ) {
                    Text("MENÚ", color = Color.White)
                }
            }
        }
    }
}
