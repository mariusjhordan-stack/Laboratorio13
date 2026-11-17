package com.example.lab13.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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

@Composable
fun Exercise3Screen() {
    var powered by remember { mutableStateOf(false) }
    var invertOrder by remember { mutableStateOf(false) }
    var jumps by remember { mutableIntStateOf(0) }

    val characterSize by animateDpAsState(
        targetValue = if (powered) 70.dp else 44.dp,
        animationSpec = spring(),
        label = "powerSize"
    )
    val jumpOffset by animateDpAsState(
        targetValue = if (powered) (-50).dp else 0.dp,
        animationSpec = spring(),
        label = "jumpOffset"
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
                text = "🏋️ Saltos: $jumps",
                color = Color(0xFFFFD54F),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "POWER-UP DE TAMAÑO Y SALTO",
                color = Color(0xFFAB47BC),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "animateDpAsState + Modifier.offset()",
                color = Color(0xFF8888AA),
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 4.dp)
            ) {
                Text(
                    text = "Invertir orden:",
                    color = Color(0xFFCCCCDD),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = invertOrder,
                    onCheckedChange = { invertOrder = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFAB47BC),
                        checkedTrackColor = Color(0xFF4A1A5E)
                    )
                )
            }
            Text(
                text = if (invertOrder) "Orden: .size() → .offset()" else "Orden: .offset() → .size()",
                color = Color(0xFF8888AA),
                fontSize = 11.sp,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Left container
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = ".offset().size()",
                        color = if (!invertOrder) Color(0xFFFF6E40) else Color(0xFF8888AA),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2A2A4A)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .then(
                                    if (!invertOrder) {
                                        Modifier
                                            .offset(y = jumpOffset)
                                            .size(characterSize)
                                    } else {
                                        Modifier
                                            .size(characterSize)
                                            .offset(y = jumpOffset)
                                    }
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFF6E40)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🤖", fontSize = 22.sp)
                        }
                    }
                }

                // Right container
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = ".size().offset()",
                        color = if (invertOrder) Color(0xFFAB47BC) else Color(0xFF8888AA),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    Box(
                        modifier = Modifier
                            .size(150.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF2A2A4A)),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(bottom = 8.dp)
                                .then(
                                    if (invertOrder) {
                                        Modifier
                                            .offset(y = jumpOffset)
                                            .size(characterSize)
                                    } else {
                                        Modifier
                                            .size(characterSize)
                                            .offset(y = jumpOffset)
                                    }
                                )
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFAB47BC)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("👾", fontSize = 22.sp)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    powered = !powered
                    if (powered) jumps++
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (powered) Color(0xFF455A64) else Color(0xFFAB47BC)
                ),
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(
                    text = if (powered) "Aterrizar 🔽" else "¡Power-Up! 🚀",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Observa cómo el orden de offset/size afecta el resultado",
                color = Color(0xFF6666AA),
                fontSize = 12.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
