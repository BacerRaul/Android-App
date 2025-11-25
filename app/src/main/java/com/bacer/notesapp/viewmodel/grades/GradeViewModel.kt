package com.bacer.notesapp.viewmodel.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacer.notesapp.data.grades.GradeEntity
import com.bacer.notesapp.data.grades.GradeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GradeViewModel(private val repo: GradeRepository) : ViewModel() {

    // Load grades
    private val _grades = MutableStateFlow<List<GradeEntity>>(emptyList())
    val grades: StateFlow<List<GradeEntity>> = _grades

    fun loadGrades(subjectId: Int) {
        viewModelScope.launch {
            _grades.value = repo.getGrades(subjectId)
        }
    }
    // ----- Load grades

    // Add grade
    private val _nameError = MutableStateFlow(false)
    val nameError: StateFlow<Boolean> = _nameError

    fun addGrade(subjectId: Int, name: String, value: Double) {
        viewModelScope.launch {

            // Check for empty name
            val cleanName = name.trim()
            if (cleanName.isEmpty()) {
                _nameError.value = true
                return@launch
            }

            // Get the grades that already exist
            val currentGrades = repo.getGrades(subjectId)

            // Check the names of the grades so that the new grade's name doesn't already exist
            if (currentGrades.any { it.name.equals(cleanName, ignoreCase = true) }) {
                _nameError.value = true
                return@launch
            }

            // Add a new grade
            repo.insertGrade(subjectId, cleanName, value)
            loadGrades(subjectId)
        }
    }

    fun clearNameError() {
        _nameError.value = false
    }
    // ----- Add grade

    // Delete grade
    fun deleteGrade(grade: GradeEntity, subjectId: Int) {
        viewModelScope.launch {
            repo.deleteGrade(grade)
            loadGrades(subjectId)
        }
    }
    // ----- Delete grade

}