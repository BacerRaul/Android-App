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
    private val _nameError = MutableStateFlow(false)
    val nameError: StateFlow<Boolean> = _nameError

    private val _imageError = MutableStateFlow(false)
    val imageError: StateFlow<Boolean> = _imageError

    fun addNote(subjectId: Int, name: String, imageUris: List<String>) {
        if (name.isBlank() || _notes.value.any { it.name == name }) {
            _nameError.value = true
            return
        }

        if (imageUris.isEmpty()) {
            _imageError.value = true
            return
        }

        viewModelScope.launch {
            repository.insertNote(subjectId, name, imageUris)
            loadNotes(subjectId)
        }
    }

    fun clearNameError() {
        _nameError.value = false
    }

    fun clearImageError() {
        _imageError.value = false
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
