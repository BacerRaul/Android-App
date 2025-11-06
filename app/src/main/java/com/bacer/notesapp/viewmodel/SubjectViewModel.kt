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

    fun loadSubjects() {
        viewModelScope.launch {
            _subjects.value = repo.getSubjects()
        }
    }

    fun addSubject(name: String) {
        viewModelScope.launch {
            repo.insertSubject(name)
            loadSubjects()
        }
    }

    fun deleteSubject(subject: SubjectEntity) {
        viewModelScope.launch {
            repo.deleteSubject(subject)
            loadSubjects()
        }
    }

}
