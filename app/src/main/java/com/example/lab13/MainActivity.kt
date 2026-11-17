package com.example.lab13

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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

data class TabItem(val label: String, val iconRes: Int)

@Composable
fun MainScreen() {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val tabs = listOf(
        TabItem("Ej. 1", android.R.drawable.ic_media_play),
        TabItem("Ej. 2", android.R.drawable.ic_menu_edit),
        TabItem("Ej. 3", android.R.drawable.ic_menu_compass),
        TabItem("Ej. 4", android.R.drawable.ic_menu_slideshow),
        TabItem("Final", android.R.drawable.ic_menu_gallery)
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, tab ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            Icon(
                                painter = painterResource(id = tab.iconRes),
                                contentDescription = tab.label
                            )
                        },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        val modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)

        when (selectedTab) {
            0 -> Exercise1Screen()
            1 -> Exercise2Screen()
            2 -> Exercise3Screen()
            3 -> Exercise4Screen()
            4 -> FinalExerciseScreen()
        }
    }
}
