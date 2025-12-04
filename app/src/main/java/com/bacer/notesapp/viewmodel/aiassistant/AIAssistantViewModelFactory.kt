package com.bacer.notesapp.viewmodel.aiassistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AIAssistantViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AIAssistantViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AIAssistantViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}