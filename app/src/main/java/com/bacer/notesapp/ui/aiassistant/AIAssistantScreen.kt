package com.bacer.notesapp.ui.aiassistant

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bacer.notesapp.ui.theme.AIAssistantGradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AIAssistantScreen(
    subjectName: String,
    noteName: String,
    onBack: () -> Unit,
    onSubmitQuestion: (String) -> Unit = {},
    isLoading: Boolean = false,
    answer: String = "",
    errorMessage: String? = null
) {
    AIAssistantGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,
            floatingActionButton = {
                Box(modifier = Modifier.fillMaxSize()) {
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 30.dp, bottom = 20.dp)
                            .size(65.dp)
                            .border(2.dp, Color.White.copy(alpha = 0.35f), CircleShape)
                            .clip(CircleShape),
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                        onClick = onBack
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            }
        ) { padding ->

            var question by remember { mutableStateOf("") }

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {

                // Title — exactly like your other screens
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = subjectName,
                        fontSize = 22.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = noteName,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Question Input — NOW 100% IDENTICAL TO YOUR NOTE CARDS
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .pointerInput(Unit) { } // keeps same behavior as other cards (optional: add gestures later)
                    ,
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                    border = BorderStroke(2.dp, Color.White.copy(alpha = 0.35f)),
                    shape = MaterialTheme.shapes.medium,
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(18.dp)
                    ) {
                        TextField(
                            value = question,
                            onValueChange = { question = it },
                            placeholder = {
                                Text(
                                    text = "Type your question here...",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White,

                                focusedPlaceholderColor = Color.White,
                                unfocusedPlaceholderColor = Color.White,

                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,

                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                            ),
                            textStyle = LocalTextStyle.current.copy(
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp,
                                color = Color.White
                            ),
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 6
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ASK GROK BUTTON — NOW FULL GLASS STYLE
                Card(
                    onClick = {
                        if (question.trim().isNotEmpty() && !isLoading) {
                            onSubmitQuestion(question.trim())
                            question = ""
                        }
                    },
                    enabled = question.trim().isNotEmpty() && !isLoading,
                    colors = CardDefaults.cardColors(
                        containerColor = if (question.trim().isNotEmpty())
                            Color.White.copy(alpha = 0.30f)
                        else
                            Color.White.copy(alpha = 0.15f)
                    ),
                    border = BorderStroke(2.dp, Color.White.copy(alpha = 0.70f)),
                    shape = MaterialTheme.shapes.medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isLoading) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                CircularProgressIndicator(
                                    color = Color.White,
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 3.dp
                                )
                                Spacer(Modifier.width(12.dp))
                                Text("Thinking...", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            }
                        } else {
                            Text(
                                text = "Ask Grok",
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Answer Area — same glass style
                if (isLoading) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.White)
                    }
                } else if (errorMessage != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF8B0000).copy(alpha = 0.3f)),
                        border = BorderStroke(2.dp, Color(0xFFFF6B6B)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = errorMessage,
                            color = Color(0xFFFF6B6B),
                            modifier = Modifier.padding(20.dp),
                            fontSize = 16.sp
                        )
                    }
                } else if (answer.isNotEmpty()) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.35f)),
                        shape = MaterialTheme.shapes.medium,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(bottom = 105.dp),
                    ) {
                        Text(
                            text = answer,
                            color = Color.White,
                            fontSize = 16.sp,
                            lineHeight = 24.sp,
                            modifier = Modifier
                                .verticalScroll(rememberScrollState())
                                .padding(20.dp)
                        )
                    }
                }
            }
        }
    }
}