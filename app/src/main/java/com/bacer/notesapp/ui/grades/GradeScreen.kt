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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color
import com.bacer.notesapp.data.grades.GradeEntity
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import com.bacer.notesapp.ui.theme.GradeGradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradeScreen(
    grades: StateFlow<List<GradeEntity>>,
    subjectName: String,
    onBack: () -> Unit,
    onAddGrade: (String, Double) -> Unit,
    onDeleteGrade: (GradeEntity) -> Unit,
    nameError: StateFlow<Boolean>,
    onClearNameError: () -> Unit
) {
    // Add grade variables
    var showAddDialog by remember { mutableStateOf(false) }
    var newGradeName by remember { mutableStateOf("") }
    var newGradeValueText by remember { mutableStateOf("") }
    // -----

    // Delete grade variables
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedGrade by remember { mutableStateOf<GradeEntity?>(null) }
    // -----

    // Screen
    GradeGradientBackground {
        Scaffold(
            containerColor = Color.Transparent, // So the default color of the screen is see-through, so the new background color can be seen

            // Back + Add grade buttons
            floatingActionButton = {
                Box(modifier = Modifier.fillMaxSize()) {
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
                        onClick = onBack
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                    // ----- Back button

                    // Add grade button
                    FloatingActionButton(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(end = 0.dp, bottom = 20.dp)
                            .size(65.dp)
                            .border(
                                width = 2.dp,
                                color = Color.White.copy(alpha = 0.35f),
                                shape = CircleShape
                            )
                            .clip(CircleShape),
                        containerColor = Color.White.copy(alpha = 0.15f),
                        contentColor = Color.White,
                        onClick = { showAddDialog = true }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add grade")
                    }
                    // ----- Add grade button
                }
            }
            // ----- Back + Add grade buttons

        ) { padding ->

            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(20.dp)
                    .fillMaxSize()
            ) {

                // Title
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
                        text = "Grades:",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                // ----- Title

                val gradeList = grades.collectAsState().value

                // Grades list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 105.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(gradeList, key = { it.id }) { grade ->

                        // Grade card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(grade.id) {
                                    detectTapGestures(
                                        onLongPress = {
                                            selectedGrade = grade
                                            showDeleteDialog = true
                                        }
                                    )
                                },
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.15f)),
                            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.35f)),
                            shape = MaterialTheme.shapes.medium,
                        ) {

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(18.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = grade.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Text(
                                        text = grade.value.toString(),
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        // ----- Grade card

                    }
                }
                // ----- Grades list

            }

            // Add grade functionality
            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showAddDialog = false
                        newGradeName = ""
                        newGradeValueText = ""
                    },
                    title = { Text("Add Grade") },
                    text = {
                        Column {
                            TextField(
                                value = newGradeName,
                                onValueChange = { newGradeName = it },
                                placeholder = { Text("Grade name") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            TextField(
                                value = newGradeValueText,
                                onValueChange = { newGradeValueText = it },
                                placeholder = { Text("Value (e.g. 9.5)") }
                            )
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            val value = newGradeValueText.toDoubleOrNull()
                            if (value != null) {
                                onAddGrade(newGradeName, value)
                            }
                            newGradeName = ""
                            newGradeValueText = ""
                            showAddDialog = false
                        }) { Text("Add") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            newGradeName = ""
                            newGradeValueText = ""
                            showAddDialog = false
                        }) { Text("Cancel") }
                    }
                )
            }

            val isDuplicate = nameError.collectAsState().value

            if (isDuplicate) {
                AlertDialog(
                    onDismissRequest = { onClearNameError() },
                    title = { Text("Invalid grade name") },
                    text = { Text("This grade name already exists or is empty.") },
                    confirmButton = {
                        TextButton(onClick = { onClearNameError() }) {
                            Text("OK")
                        }
                    }
                )
            }
            // ----- Add grade functionality

            // Delete grade functionality
            if (showDeleteDialog && selectedGrade != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        selectedGrade = null
                    },
                    title = { Text("Delete Grade") },
                    text = { Text("Are you sure you want to delete \"${selectedGrade!!.name}\"?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDeleteGrade(selectedGrade!!)
                                showDeleteDialog = false
                                selectedGrade = null
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error
                            )
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = {
                                showDeleteDialog = false
                                selectedGrade = null
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
            // ----- Delete grade functionality

        }
    }
    // ----- Screen

}