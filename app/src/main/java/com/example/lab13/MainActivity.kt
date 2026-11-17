package com.example.lab13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab13.screens.Exercise1Screen
import com.example.lab13.screens.Exercise2Screen
import com.example.lab13.screens.Exercise3Screen
import com.example.lab13.screens.Exercise4Screen
import com.example.lab13.screens.FinalExerciseScreen
import com.example.lab13.ui.theme.Lab13Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Lab13Theme {
                MainScreen()
            }
        }
    }
}

data class TabItem(val label: String, val number: String, val activeColor: Color)

@Composable
fun MainScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem("Ej. 1", "1", Color(0xFFFF6E40)),
        TabItem("Ej. 2", "2", Color(0xFF448AFF)),
        TabItem("Ej. 3", "3", Color(0xFFAB47BC)),
        TabItem("Ej. 4", "4", Color(0xFF66BB6A)),
        TabItem("Final", "5", Color(0xFFFFD54F))
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1A1A2E)
            ) {
                tabs.forEachIndexed { index, tab ->
                    val isSelected = selectedTab == index
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = index },
                        icon = {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (isSelected) tab.activeColor
                                        else Color(0xFF2A2A4A)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = tab.number,
                                    color = if (isSelected) Color(0xFF1A1A2E) else Color(0xFF8888AA),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        },
                        label = {
                            Text(
                                tab.label,
                                color = if (isSelected) tab.activeColor else Color(0xFF8888AA),
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedTab) {
                0 -> Exercise1Screen()
                1 -> Exercise2Screen()
                2 -> Exercise3Screen()
                3 -> Exercise4Screen()
                4 -> FinalExerciseScreen()
            }
        }
    }
}
