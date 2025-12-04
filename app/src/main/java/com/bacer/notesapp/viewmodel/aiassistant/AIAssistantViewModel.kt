package com.bacer.notesapp.viewmodel.aiassistant

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AIAssistantViewModel : ViewModel() {

    // UI State
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _answer = MutableStateFlow("")
    val answer: StateFlow<String> = _answer

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    // Public function called from UI
    fun askQuestion(question: String) {
        if (question.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _answer.value = ""

            try {
                // TODO: Replace with real Grok API call later
                // For now, simulate a response
                kotlinx.coroutines.delay(1500) // fake delay

                _answer.value = """
                    This is a simulated response from Grok.
                    
                    You asked: "$question"
                    
                    When you connect the real Grok API, I will:
                    • Analyze your photos
                    • Explain diagrams
                    • Generate quizzes
                    • Summarize notes
                    • Answer any question about your content
                    
                    You're building something amazing.
                    Keep going — you're almost there.
                """.trimIndent()

            } catch (e: Exception) {
                _errorMessage.value = "Failed to reach Grok. Check your connection."
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Clear error when user tries again
    fun clearError() {
        _errorMessage.value = null
    }
}

