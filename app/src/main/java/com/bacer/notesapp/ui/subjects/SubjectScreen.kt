package com.bacer.notesapp.ui.subjects

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.bacer.notesapp.data.subjects.SubjectEntity
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.Alignment
import com.bacer.notesapp.ui.theme.SubjectGradientBackground

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectScreen(
    subjects: StateFlow<List<SubjectEntity>>,
    onAddSubject: (String) -> Unit,
    onDeleteSubject: (SubjectEntity) -> Unit,
    nameError: StateFlow<Boolean>,
    onClearNameError: () -> Unit,
    onGradesClick: (Int) -> Unit,
    onNotesClick: (Int) -> Unit
) {
    // Add subject variables
    var showAddDialog by remember { mutableStateOf(false) }
    var newSubjectName by remember { mutableStateOf("") }
    // -----

    // Delete subject variables
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedSubject by remember { mutableStateOf<SubjectEntity?>(null) }
    // -----

    // Expand subject variables
    var expandedSubjectId by remember { mutableStateOf<Int?>(null) }
    // -----

    // Screen
    SubjectGradientBackground {
        Scaffold(
            containerColor = Color.Transparent, // So the default color of the screen is see-through, so the new background color can be seen

            // Add subject button
            floatingActionButton = {
                Box(modifier = Modifier.fillMaxSize()) {
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
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add subject")
                    }
                }
            }
            // ----- Add subject button

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
                        text = "Welcome Back!",
                        fontSize = 22.sp,
                        color = Color.White.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Subjects",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                // ----- Title

                val subjectList = subjects.collectAsState().value

                // Subjects list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 105.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(subjectList, key = { it.id }) { subject ->

                        // Subject card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(subject.id) {
                                    detectTapGestures(
                                        onTap = { expandedSubjectId = if (expandedSubjectId == subject.id) null else subject.id },
                                        onLongPress = {
                                            selectedSubject = subject
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
                                Text(
                                    text = subject.name,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.White
                                )
                            }
                        }

                        // Expanded subject cards
                        if (expandedSubjectId == subject.id) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(start = 14.dp, end = 14.dp, top = 6.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                listOf("Notes", "Grades").forEach { label ->
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(36.dp)
                                            .pointerInput(subject.id) {
                                                detectTapGestures {
                                                    when (label) {
                                                        "Grades" -> onGradesClick(subject.id)
                                                        "Notes" -> onNotesClick(subject.id)
                                                    }
                                                }
                                            },
                                        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.30f)),
                                        border = BorderStroke(2.dp, Color.White.copy(alpha = 0.70f)),
                                        shape = MaterialTheme.shapes.medium,
                                    ) {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = label,
                                                fontSize = 14.sp,
                                                color = Color.White
                                            )
                                        }
                                    }
                                }
                            }
                        }
                        // ----- Expanded subject cards

                        // ----- Subject card

                    }
                }
                // ----- Subjects list

            }

            // Add subject functionality
            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = { showAddDialog = false },
                    title = { Text("Add Subject") },
                    text = {
                        TextField(
                            value = newSubjectName,
                            onValueChange = { newSubjectName = it },
                            placeholder = { Text("Subject name") }
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            onAddSubject(newSubjectName)
                            newSubjectName = ""
                            showAddDialog = false
                        }) { Text("Add") }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            newSubjectName = ""
                            showAddDialog = false
                        }) { Text("Cancel") }
                    }
                )
            }

            val isDuplicate = nameError.collectAsState().value

            if (isDuplicate) {
                AlertDialog(
                    onDismissRequest = { onClearNameError() },
                    title = { Text("Invalid subject name") },
                    text = { Text("This subject name already exists or is empty.") },
                    confirmButton = {
                        TextButton(onClick = { onClearNameError() }) {
                            Text("OK")
                        }
                    }
                )
            }
            // ----- Add subject functionality

            // Delete subject functionality
            if (showDeleteDialog && selectedSubject != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        selectedSubject = null
                    },
                    title = { Text("Delete Subject") },
                    text = { Text("Are you sure you want to delete \"${selectedSubject!!.name}\"?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDeleteSubject(selectedSubject!!)
                                showDeleteDialog = false
                                selectedSubject = null
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
                                selectedSubject = null
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
            // ----- Delete subject functionality

        }
    }
    // ----- Screen

}
