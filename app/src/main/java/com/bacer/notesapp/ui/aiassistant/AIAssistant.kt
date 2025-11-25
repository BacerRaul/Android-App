package com.bacer.notesapp.ui.aiassistant

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.bacer.notesapp.ui.theme.AIAssistantGradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen() {

    AIAssistantGradientBackground {
        Scaffold(
            containerColor = Color.Transparent
        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {

            }

        }
    }
}
