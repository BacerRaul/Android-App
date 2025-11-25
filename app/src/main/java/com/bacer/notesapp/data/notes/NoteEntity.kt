package com.bacer.notesapp.data.notes

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val subjectId: Int,
    val name: String,
    val imageUris: List<String> // stored as List<String> using the converter
)
