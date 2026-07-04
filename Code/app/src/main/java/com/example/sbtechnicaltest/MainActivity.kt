package com.example.sbtechnicaltest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.sbtechnicaltest.core.ui.theme.SBTechnicalTestTheme
import com.example.sbtechnicaltest.navigation.AppNavGraph
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SBTechnicalTestTheme {
                AppNavGraph()
            }
        }
    }
}
