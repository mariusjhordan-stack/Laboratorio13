package com.example.lab13.screens

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
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
fun Exercise3Screen() {
    var expanded by remember { mutableStateOf(false) }
    var invertOrder by remember { mutableStateOf(false) }

    val size by animateDpAsState(
        targetValue = if (expanded) 160.dp else 80.dp,
        animationSpec = spring(),
        label = "sizeAnimation"
    )
    val offsetX by animateDpAsState(
        targetValue = if (expanded) 100.dp else 0.dp,
        animationSpec = spring(),
        label = "offsetAnimation"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Ejercicio 3: animateDpAsState",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 8.dp)
        ) {
            Text("Invertir orden (offset ↔ size): ")
            Switch(checked = invertOrder, onCheckedChange = { invertOrder = it })
        }

        Text(
            text = if (invertOrder) ".size() → .offset()" else ".offset() → .size()",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (!invertOrder) "Actual" else "Comparación",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = if (!invertOrder) {
                            Modifier
                                .offset(x = offsetX)
                                .size(size)
                        } else {
                            Modifier
                                .size(size)
                                .offset(x = offsetX)
                        }
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFFE91E63))
                    )
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = if (invertOrder) "Actual" else "Comparación",
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.TopStart
                ) {
                    Box(
                        modifier = if (invertOrder) {
                            Modifier
                                .offset(x = offsetX)
                                .size(size)
                        } else {
                            Modifier
                                .size(size)
                                .offset(x = offsetX)
                        }
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF9C27B0))
                    )
                }
            }
        }

        Button(
            onClick = { expanded = !expanded },
            modifier = Modifier.padding(top = 24.dp)
        ) {
            Text(if (expanded) "Reducir" else "Expandir")
        }
    }
}
