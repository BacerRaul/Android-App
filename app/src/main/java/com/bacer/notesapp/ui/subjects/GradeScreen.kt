package com.bacer.notesapp.ui.grades

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bacer.notesapp.ui.theme.GradeGradientBackground
import androidx.compose.ui.graphics.Color
import com.bacer.notesapp.data.GradeEntity
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures
import kotlinx.coroutines.flow.collectLatest
import com.bacer.notesapp.viewmodel.GradeViewModel
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import com.bacer.notesapp.viewmodel.SubjectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen(
    subjectId: Int,
    onBack: () -> Unit,
    viewModel: SubjectViewModel
) {
    val subject by viewModel.getSubjectById(subjectId).collectAsState(initial = null)

    val grades = listOf(
        "Test 1: 8.50",
        "Assignment: 9.20",
        "Midterm: 7.90",
        "Project: 10",
        "Final Exam: 9.75"
    )

    // Screen
    GradeGradientBackground {
        Scaffold(
            containerColor = Color.Transparent,

            floatingActionButton = {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {

                    // Back button
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 30.dp, bottom = 20.dp)
                            .size(65.dp)
                            .border(
                                width = 2.dp,
                                color = Color.White.copy(alpha = 0.35f),
                                shape = CircleShape
                            )
                            .clip(CircleShape),
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,

                        onClick = { onBack() }
                    )

                    {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    // ----- Back button

                    // Add grade button
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 30.dp, bottom = 20.dp)
                            .size(65.dp)
                            .border(
                                width = 2.dp,
                                color = Color.White.copy(alpha = 0.35f),
                                shape = CircleShape
                            )
                            .clip(CircleShape),
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                        onClick = {
                            // TODO: open AddGrade dialog (we add later after entity + viewmodel)
                        }
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add grade")
                    }
                    // ----- Add grade button

                }
            }

        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {

                // Title
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = subject?.name ?: "Grades",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                // ----- Title

                // Grades list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(grades) { grade ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.35f)),
                            shape = MaterialTheme.shapes.medium
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = grade,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
                // Grades list

            }
        }
    }
    // ----- Screen
}
