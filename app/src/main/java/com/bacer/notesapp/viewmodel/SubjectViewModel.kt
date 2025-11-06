package com.bacer.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacer.notesapp.data.SubjectRepository
import com.bacer.notesapp.data.SubjectEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SubjectViewModel(private val repo: SubjectRepository) : ViewModel() {

    private val _subjects = MutableStateFlow<List<SubjectEntity>>(emptyList())
    val subjects: StateFlow<List<SubjectEntity>> = _subjects

    // Load subjects
    fun loadSubjects() {
        viewModelScope.launch {
            _subjects.value = repo.getSubjects()
        }
    }
    // ----- Load subjects

    // Add subject
    private val _nameError = MutableStateFlow(false)
    val nameError: StateFlow<Boolean> = _nameError


    fun addSubject(name: String) {
        viewModelScope.launch {

            // Check for empty name
            val cleanName = name.trim()
            if (cleanName.isEmpty()) {
                _nameError.value = true
                return@launch
            }

            // Get the subjects that already exist
            val currentSubjects = repo.getSubjects()

            // Check the names of the subjects so that the new subject's name doesn't already exist
            if (currentSubjects.any { it.name.equals(name, ignoreCase = true) }) {
                _nameError.value = true
                return@launch
            }

            // Add a new subject
            repo.insertSubject(name)
            loadSubjects()
        }
    }

    fun clearNameError() {
        _nameError.value = false
    }
    // ----- Add subject

    // Delete subject
    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            repo.deleteSubject(subject)
            loadSubjects()
        }
    }
    // ----- Delete subject

}
