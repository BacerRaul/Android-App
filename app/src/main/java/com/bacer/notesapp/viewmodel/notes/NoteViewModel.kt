package com.bacer.notesapp.viewmodel.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacer.notesapp.data.notes.NoteEntity
import com.bacer.notesapp.data.notes.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteViewModel(private val repository: NoteRepository) : ViewModel() {

    // Load notes
    private val _notes = MutableStateFlow<List<NoteEntity>>(emptyList())
    val notes: StateFlow<List<NoteEntity>> = _notes

    fun loadNotes(subjectId: Int) {
        viewModelScope.launch {
            _notes.value = repository.getNotes(subjectId)
        }
    }
    // ----- Load notes

    // Add notes
    fun addNote(subjectId: Int, name: String, imageUris: List<String>) {
        viewModelScope.launch {
            repository.insertNote(subjectId, name, imageUris)
            loadNotes(subjectId)
        }
    }
    // ----- Add notes

    // Delete notes
    fun deleteNote(note: NoteEntity, subjectId: Int) {
        viewModelScope.launch {
            repository.deleteNote(note)
            loadNotes(subjectId)
        }
    }
    // ----- Delete notes
}
