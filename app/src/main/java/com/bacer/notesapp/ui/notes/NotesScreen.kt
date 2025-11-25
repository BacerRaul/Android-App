package com.bacer.notesapp.ui.notes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.bacer.notesapp.data.notes.NoteEntity
import com.bacer.notesapp.ui.theme.NotesGradientBackground
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesScreen(
    notes: StateFlow<List<NoteEntity>>,
    subjectName: String,
    onBack: () -> Unit,
    onAddNote: (String, List<String>) -> Unit,
    onDeleteNote: (NoteEntity) -> Unit
) {
    // Add note variables
    var showAddDialog by remember { mutableStateOf(false) }
    var newNoteName by remember { mutableStateOf("") }
    var selectedImages by remember { mutableStateOf<List<Uri>>(emptyList()) }
    // -----

    // Delete note variables
    var showDeleteDialog by remember { mutableStateOf(false) }
    var selectedNote by remember { mutableStateOf<NoteEntity?>(null) }
    // -----

    // Image picker launcher
    val imagePickerLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            selectedImages = uris
        }

    NotesGradientBackground {
        Scaffold(
            containerColor = Color.Transparent, // So the default color of the screen is see-through, so the new background color can be seen

            // Back + Add note buttons
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

                    // Add note button
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
                        onClick = { showAddDialog = true }
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add note")
                    }
                    // ----- Add note button
                }
            }
            // ----- Back + Add note buttons

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
                        text = subjectName,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                // ----- Title

                val noteList = notes.collectAsState().value

                // Notes list
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(noteList, key = { it.id }) { note ->

                        // Note card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .pointerInput(note.id) {
                                    detectTapGestures(
                                        onLongPress = {
                                            selectedNote = note
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
                                        text = note.name,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.White
                                    )
                                    Text(
                                        text = "${note.imageUris.size} photos",
                                        fontSize = 16.sp,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                        // ----- Note card

                    }
                }
                // ----- Notes list

            }

            // Add note functionality
            if (showAddDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showAddDialog = false
                        newNoteName = ""
                        selectedImages = emptyList()
                    },
                    title = { Text("Add Note") },
                    text = {
                        Column {
                            TextField(
                                value = newNoteName,
                                onValueChange = { newNoteName = it },
                                placeholder = { Text("Note name") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            Button(
                                onClick = { imagePickerLauncher.launch("image/*") }
                            ) {
                                Text("Select Images")
                            }

                            if (selectedImages.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = "${selectedImages.size} images selected",
                                    color = Color.White
                                )
                            }
                        }
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            onAddNote(
                                newNoteName,
                                selectedImages.map { it.toString() }
                            )
                            newNoteName = ""
                            selectedImages = emptyList()
                            showAddDialog = false
                        }) {
                            Text("Add")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            showAddDialog = false
                            newNoteName = ""
                            selectedImages = emptyList()
                        }) {
                            Text("Cancel")
                        }
                    }
                )
            }
            // ----- Add note functionality

            // Delete note functionality
            if (showDeleteDialog && selectedNote != null) {
                AlertDialog(
                    onDismissRequest = {
                        showDeleteDialog = false
                        selectedNote = null
                    },
                    title = { Text("Delete Note") },
                    text = { Text("Are you sure you want to delete \"${selectedNote!!.name}\"?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                onDeleteNote(selectedNote!!)
                                showDeleteDialog = false
                                selectedNote = null
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
                                selectedNote = null
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
            // ----- Delete note functionality

        }
    }
    // ----- Screen

}

