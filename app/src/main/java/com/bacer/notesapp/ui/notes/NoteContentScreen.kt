package com.bacer.notesapp.ui.notes

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.bacer.notesapp.data.notes.NoteEntity
import com.bacer.notesapp.ui.theme.NotesGradientBackground
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteContentScreen(
    noteName: String,
    imageUris: List<String>,
    onBack: () -> Unit,
) {

    // Screen
    NotesGradientBackground {
        Scaffold(
            containerColor = Color.Transparent, // So the default color of the screen is see-through, so the new background color can be seen

            // Back button
            floatingActionButton = {
                Box(modifier = Modifier.fillMaxSize()) {
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
                }
            }
            // ----- Back button

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
                        text = noteName,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                // ----- Title

                // Images
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(imageUris) { uriString ->
                        val uri = Uri.parse(uriString)

                        Card(
                            modifier = Modifier.
                                fillMaxWidth()
                                .padding(top = 10.dp, bottom = 10.dp),
                            colors = CardDefaults.cardColors(
                                Color.White.copy(alpha = 0.15f)
                            ),
                            border = BorderStroke(2.dp, Color.White.copy(alpha = 0.35f))
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(250.dp)
                                    .padding(8.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }

                // ----- Images

            }
        }
    }
    // ----- Screen

}

