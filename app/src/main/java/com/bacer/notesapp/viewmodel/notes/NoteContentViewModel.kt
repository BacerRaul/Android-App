package com.bacer.notesapp.viewmodel.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacer.notesapp.data.notes.NoteEntity
import com.bacer.notesapp.data.notes.NoteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NoteContentViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _note = MutableStateFlow<NoteEntity?>(null)
    val note: StateFlow<NoteEntity?> = _note

    fun loadNote(id: Int) {
        viewModelScope.launch {
            _note.value = repository.getNoteById(id)
        }
    }
}


