package ru.atol.os.tspiot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import ru.atol.os.tspiot.ui.MainScreen
import ru.atol.os.tspiot.ui.theme.ESMDemoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ESMDemoTheme {
                MainScreen(context = this.applicationContext)
            }
        }
    }
}