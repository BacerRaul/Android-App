package com.bacer.notesapp.viewmodel.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bacer.notesapp.data.notes.NoteRepository

class NoteContentViewModelFactory(
    private val repository: NoteRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return NoteContentViewModel(repository) as T
    }
}


