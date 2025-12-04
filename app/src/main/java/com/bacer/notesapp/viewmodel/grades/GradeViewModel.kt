package com.bacer.notesapp.viewmodel.grades

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bacer.notesapp.data.grades.GradeEntity
import com.bacer.notesapp.data.grades.GradeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class GradeViewModel(private val repository: GradeRepository) : ViewModel() {

    // Load grades
    private val _grades = MutableStateFlow<List<GradeEntity>>(emptyList())
    val grades: StateFlow<List<GradeEntity>> = _grades

    fun loadGrades(subjectId: Int) {
        viewModelScope.launch {
            _grades.value = repository.getGrades(subjectId)
        }
    }
    // ----- Load grades

    // Add grade
    private val _nameError = MutableStateFlow(false)
    val nameError: StateFlow<Boolean> = _nameError

    private val _valueError = MutableStateFlow(false)
    val valueError: StateFlow<Boolean> = _valueError

    fun addGrade(subjectId: Int, name: String, value: Double) {
        viewModelScope.launch {

            // Check for empty name
            val cleanName = name.trim()
            if (cleanName.isEmpty()) {
                _nameError.value = true
                return@launch
            }

            // Get the grades that already exist and check their names so the new grade has a unique name
            val currentGrades = repository.getGrades(subjectId)
            if (currentGrades.any { it.name.equals(cleanName, ignoreCase = true) }) {
                _nameError.value = true
                return@launch
            }

            // Check for invalid value
            if (value < 0 || value >10 || value.isNaN()) {
                _valueError.value = true
                return@launch
            }

            // Add a new grade
            repository.insertGrade(subjectId, cleanName, value)
            loadGrades(subjectId)
        }
    }

    fun clearNameError() {
        _nameError.value = false
    }

    fun clearValueError() {
        _valueError.value = false
    }
    // ----- Add grade

    // Delete grade
    fun deleteGrade(grade: GradeEntity, subjectId: Int) {
        viewModelScope.launch {
            repository.deleteGrade(grade)
            loadGrades(subjectId)
        }
    }
    // ----- Delete grade

}