package com.example.lab13.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private enum class GamePhase { Menu, Playing, GameOver }

private data class FallingObject(
    val x: Float,
    val y: Float,
    val id: Int,
    val isEnergy: Boolean = false,
    val rotation: Float = 0f,
    val sizeVariant: Float = 1f
)

private data class Star(val x: Float, val y: Float, val alpha: Float, val size: Float)

private const val SHIP_Y_FRACTION = 0.83f
private const val OBSTACLE_RADIUS_BASE = 14f
private const val ENERGY_ORB_RADIUS_BASE = 12f

@Composable
fun FinalExerciseScreen() {
    var gamePhase by remember { mutableStateOf(GamePhase.Menu) }
    var score by remember { mutableIntStateOf(0) }
    var lives by remember { mutableIntStateOf(3) }
    var shipX by remember { mutableFloatStateOf(0.5f) }
    val obstacles = remember { mutableStateListOf<FallingObject>() }
    var idCounter by remember { mutableIntStateOf(0) }
    var showDamage by remember { mutableStateOf(false) }
    var showEnergy by remember { mutableStateOf(false) }
    var tick by remember { mutableIntStateOf(0) }
    var arenaWidthPx by remember { mutableFloatStateOf(1f) }
    var arenaHeightPx by remember { mutableFloatStateOf(1f) }
    val stars = remember {
        List(60) {
            Star(
                x = Random.nextFloat(),
                y = Random.nextFloat(),
                alpha = Random.nextFloat() * 0.6f + 0.2f,
                size = Random.nextFloat() * 2f + 0.5f
            )
        }
    }

    // AnimatedContent: Menu / Playing / GameOver
    AnimatedContent(
        targetState = gamePhase,
        transitionSpec = {
            fadeIn(tween(400)) togetherWith fadeOut(tween(200))
        },
        label = "gamePhaseTransition"
    ) { phase ->
        when (phase) {
            GamePhase.Menu -> SpaceMenu(
                stars = stars,
                onStart = {
                    score = 0
                    lives = 3
                    shipX = 0.5f
                    obstacles.clear()
                    idCounter = 0
                    tick = 0
                    gamePhase = GamePhase.Playing
                }
            )

            GamePhase.Playing -> {
                val density = LocalDensity.current

                // animateColorAsState: subtle ship glow on damage/energy
                val shipGlow by animateColorAsState(
                    targetValue = when {
                        showDamage -> Color(0xFFFF6E40)
                        showEnergy -> Color(0xFF64FFDA)
                        else -> Color(0xFF448AFF)
                    },
                    animationSpec = tween(250),
                    label = "shipGlow"
                )

                val shipGlowAlpha by animateFloatAsState(
                    targetValue = if (showDamage || showEnergy) 0.7f else 0.3f,
                    animationSpec = tween(250),
                    label = "glowAlpha"
                )

                // animateDpAsState: ship pulses on energy collect
                val shipSize by animateDpAsState(
                    targetValue = if (showEnergy) 54.dp else 46.dp,
                    animationSpec = spring(),
                    label = "shipPulse"
                )

                LaunchedEffect(Unit) {
                    while (gamePhase == GamePhase.Playing) {
                        delay(50L)
                        tick++

                        if (tick % 12 == 0) {
                            val isE = Random.nextFloat() < 0.22f
                            obstacles.add(
                                FallingObject(
                                    x = Random.nextFloat() * 0.9f + 0.05f,
                                    y = -0.05f,
                                    id = idCounter++,
                                    isEnergy = isE,
                                    rotation = Random.nextFloat() * 360f,
                                    sizeVariant = Random.nextFloat() * 0.4f + 0.8f
                                )
                            )
                        }

                        val updated = obstacles.map {
                            it.copy(
                                y = it.y + 0.012f,
                                rotation = it.rotation + if (it.isEnergy) 3f else 2f
                            )
                        }
                        obstacles.clear()
                        obstacles.addAll(updated)

                        val shipCenterXPx = shipX * arenaWidthPx
                        val shipCenterYPx = SHIP_Y_FRACTION * arenaHeightPx
                        val shipRadiusPx = with(density) { shipSize.toPx() / 2f }

                        val toRemove = mutableListOf<Int>()
                        for (obs in obstacles) {
                            val obsCenterXPx = obs.x * arenaWidthPx
                            val obsCenterYPx = obs.y * arenaHeightPx
                            val obsRadiusPx = if (obs.isEnergy) {
                                ENERGY_ORB_RADIUS_BASE * obs.sizeVariant
                            } else {
                                OBSTACLE_RADIUS_BASE * obs.sizeVariant
                            }
                            val dx = obsCenterXPx - shipCenterXPx
                            val dy = obsCenterYPx - shipCenterYPx
                            val dist = sqrt(dx * dx + dy * dy)
                            if (dist < shipRadiusPx + obsRadiusPx) {
                                toRemove.add(obs.id)
                                if (obs.isEnergy) {
                                    score += 10
                                    showEnergy = true
                                } else {
                                    lives--
                                    showDamage = true
                                }
                            }
                        }
                        obstacles.removeAll { it.id in toRemove }
                        obstacles.removeAll { it.y > 1.1f }

                        if (tick % 20 == 0) score++

                        if (showDamage) {
                            delay(250)
                            showDamage = false
                        }
                        if (showEnergy) {
                            delay(250)
                            showEnergy = false
                        }

                        if (lives <= 0) {
                            gamePhase = GamePhase.GameOver
                        }
                    }
                }

                GameArena(
                    stars = stars,
                    tick = tick,
                    shipX = shipX,
                    shipSize = shipSize,
                    shipGlow = shipGlow,
                    shipGlowAlpha = shipGlowAlpha,
                    obstacles = obstacles,
                    score = score,
                    lives = lives,
                    showDamage = showDamage,
                    onDrag = { delta -> shipX = (shipX + delta).coerceIn(0.06f, 0.94f) },
                    onArenaSize = { w, h ->
                        arenaWidthPx = w
                        arenaHeightPx = h
                    }
                )
            }

            GamePhase.GameOver -> SpaceGameOver(
                stars = stars,
                score = score,
                onRestart = {
                    score = 0
                    lives = 3
                    shipX = 0.5f
                    obstacles.clear()
                    idCounter = 0
                    tick = 0
                    gamePhase = GamePhase.Playing
                },
                onMenu = { gamePhase = GamePhase.Menu }
            )
        }
    }
}

@Composable
private fun StarField(stars: List<Star>, tick: Int = 0) {
    Canvas(modifier = Modifier.fillMaxSize()) {
        for (star in stars) {
            val flicker = (sin((tick * 0.05f + star.x * 10f).toDouble()) * 0.3f + 0.7f).toFloat()
            drawCircle(
                color = Color.White.copy(alpha = star.alpha * flicker),
                radius = star.size,
                center = Offset(star.x * size.width, star.y * size.height)
            )
        }
    }
}

@Composable
private fun SpaceMenu(stars: List<Star>, onStart: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E1A)),
        contentAlignment = Alignment.Center
    ) {
        StarField(stars)

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("🚀", fontSize = 72.sp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "ESCAPE ESPACIAL",
                color = Color(0xFF64FFDA),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Esquiva meteoritos ☄️ · Atrapa energía ⚡",
                color = Color(0xFF8899BB),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Desliza para mover la nave",
                color = Color(0xFF667799),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64FFDA))
            ) {
                Text("JUGAR", color = Color(0xFF0A0E1A), fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }
        }
    }
}

@Composable
private fun GameArena(
    stars: List<Star>,
    tick: Int,
    shipX: Float,
    shipSize: androidx.compose.ui.unit.Dp,
    shipGlow: Color,
    shipGlowAlpha: Float,
    obstacles: List<FallingObject>,
    score: Int,
    lives: Int,
    showDamage: Boolean,
    onDrag: (Float) -> Unit,
    onArenaSize: (Float, Float) -> Unit
) {
    val density = LocalDensity.current

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E1A))
            .pointerInput(Unit) {
                detectDragGestures { _, dragAmount ->
                    onDrag(dragAmount.x / size.width.toFloat())
                }
            }
    ) {
        val arenaWidthPx = with(density) { maxWidth.toPx() }
        val arenaHeightPx = with(density) { maxHeight.toPx() }

        LaunchedEffect(arenaWidthPx, arenaHeightPx) {
            onArenaSize(arenaWidthPx, arenaHeightPx)
        }

        val shipSizePx = with(density) { shipSize.toPx() }
        val shipCenterXPx = shipX * arenaWidthPx
        val shipCenterYPx = SHIP_Y_FRACTION * arenaHeightPx

        // Star background + obstacles drawn on canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            for (star in stars) {
                val flicker = (sin((tick * 0.05f + star.x * 10f).toDouble()) * 0.3f + 0.7f).toFloat()
                drawCircle(
                    color = Color.White.copy(alpha = star.alpha * flicker),
                    radius = star.size,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }

            for (obs in obstacles) {
                val cx = obs.x * size.width
                val cy = obs.y * size.height
                if (obs.isEnergy) {
                    drawEnergyOrb(cx, cy, obs.rotation, obs.sizeVariant)
                } else {
                    drawMeteor(cx, cy, obs.rotation, obs.sizeVariant)
                }
            }

            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(
                        shipGlow.copy(alpha = shipGlowAlpha),
                        shipGlow.copy(alpha = shipGlowAlpha * 0.3f),
                        Color.Transparent
                    ),
                    center = Offset(shipCenterXPx, shipCenterYPx),
                    radius = shipSizePx * 1.3f
                ),
                radius = shipSizePx * 1.3f,
                center = Offset(shipCenterXPx, shipCenterYPx)
            )
        }

        // HUD with statusBarsPadding
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 8.dp),
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

        // Ship composable (centered on shipCenterX, shipCenterY)
        Box(
            modifier = Modifier
                .offset {
                    IntOffset(
                        (shipCenterXPx - shipSizePx / 2f).roundToInt(),
                        (shipCenterYPx - shipSizePx / 2f).roundToInt()
                    )
                }
                .size(shipSize),
            contentAlignment = Alignment.Center
        ) {
            Text("🚀", fontSize = 28.sp)
        }

        // AnimatedVisibility: damage warning popup
        AnimatedVisibility(
            visible = showDamage,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically(),
            modifier = Modifier
                .align(Alignment.TopCenter)
                .statusBarsPadding()
                .padding(top = 48.dp)
        ) {
            Text(
                text = "⚠️ ¡DAÑO!",
                color = Color(0xFFFF6E40),
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xCC1A1A2E))
                    .padding(horizontal = 24.dp, vertical = 8.dp)
            )
        }
    }
}

private fun DrawScope.drawMeteor(cx: Float, cy: Float, rotation: Float, scale: Float) {
    val baseRadius = 14f * scale
    rotate(rotation, pivot = Offset(cx, cy)) {
        val path = Path().apply {
            moveTo(cx, cy - baseRadius * 1.1f)
            lineTo(cx + baseRadius * 0.7f, cy - baseRadius * 0.5f)
            lineTo(cx + baseRadius * 1.0f, cy + baseRadius * 0.2f)
            lineTo(cx + baseRadius * 0.5f, cy + baseRadius * 0.9f)
            lineTo(cx - baseRadius * 0.3f, cy + baseRadius * 1.0f)
            lineTo(cx - baseRadius * 0.9f, cy + baseRadius * 0.4f)
            lineTo(cx - baseRadius * 0.8f, cy - baseRadius * 0.3f)
            close()
        }
        drawPath(path, Color(0xFF8D6E63))
        drawPath(path, Brush.radialGradient(
            colors = listOf(Color(0xFFBCAAA4), Color(0xFF5D4037)),
            center = Offset(cx - baseRadius * 0.2f, cy - baseRadius * 0.2f),
            radius = baseRadius * 1.5f
        ))
        drawCircle(Color(0xFF4E342E), radius = baseRadius * 0.2f, center = Offset(cx + baseRadius * 0.3f, cy))
        drawCircle(Color(0xFF4E342E), radius = baseRadius * 0.12f, center = Offset(cx - baseRadius * 0.2f, cy + baseRadius * 0.4f))
    }
}

private fun DrawScope.drawEnergyOrb(cx: Float, cy: Float, rotation: Float, scale: Float) {
    val r = 12f * scale
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                Color(0xFF64FFDA),
                Color(0xFF00BFA5).copy(alpha = 0.6f),
                Color.Transparent
            ),
            center = Offset(cx, cy),
            radius = r * 2.5f
        ),
        radius = r * 2.5f,
        center = Offset(cx, cy)
    )
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(Color.White, Color(0xFF64FFDA)),
            center = Offset(cx, cy),
            radius = r
        ),
        radius = r,
        center = Offset(cx, cy)
    )
    val angle = Math.toRadians(rotation.toDouble())
    val sparkDist = r * 1.6f
    for (i in 0 until 4) {
        val a = angle + i * Math.PI / 2
        drawCircle(
            color = Color(0xFF64FFDA).copy(alpha = 0.6f),
            radius = 2.5f,
            center = Offset(
                cx + (cos(a) * sparkDist).toFloat(),
                cy + (sin(a) * sparkDist).toFloat()
            )
        )
    }
}

@Composable
private fun SpaceGameOver(
    stars: List<Star>,
    score: Int,
    onRestart: () -> Unit,
    onMenu: () -> Unit
) {
    var showPanel by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { showPanel = true }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E1A)),
        contentAlignment = Alignment.Center
    ) {
        StarField(stars)

        // AnimatedVisibility: game over panel
        AnimatedVisibility(
            visible = showPanel,
            enter = fadeIn(tween(600)) + expandVertically(tween(400)),
            exit = fadeOut()
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text("💥", fontSize = 64.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "GAME OVER",
                    color = Color(0xFFFF6E40),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color(0xFF1A2A3A))
                        .padding(horizontal = 24.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = "⭐ $score pts",
                        color = Color(0xFFFFD54F),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    onClick = onRestart,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF64FFDA)),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text("REINTENTAR", color = Color(0xFF0A0E1A), fontWeight = FontWeight.Bold)
                }
                Button(
                    onClick = onMenu,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2A3A4A))
                ) {
                    Text("MENÚ", color = Color.White)
                }
            }
        }
    }
}
