package com.bacer.notesapp.viewmodel.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bacer.notesapp.data.notes.NoteRepository
import com.bacer.notesapp.viewmodel.notes.NoteViewModel

class NoteViewModelFactory(private val repository: NoteRepository)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteViewModel(repository) as T
    }
}
