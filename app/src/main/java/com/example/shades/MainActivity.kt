package com.example.shades

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.shades.MyAppNavigation.Navigation
import com.example.shades.ui.theme.ShadesTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShadesTheme {
                Navigation()
            }
        }
    }
}
