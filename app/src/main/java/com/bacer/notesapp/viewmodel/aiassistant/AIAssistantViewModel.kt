package com.bacer.notesapp.viewmodel.aiassistant

import android.util.Base64
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.bacer.notesapp.api.*
import java.io.File

class AIAssistantViewModel : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _answer = MutableStateFlow("")
    val answer: StateFlow<String> = _answer

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    fun askQuestion(question: String, imagePaths: List<String> = emptyList()) {
        if (question.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _answer.value = ""

            try {
                val contents = mutableListOf<Content>()

                // Add images as base64 (only way Grok accepts them)
                imagePaths.forEach { path ->
                    val file = File(path)
                    if (file.exists() && file.length() < 20_000_000) { // < 20 MB
                        try {
                            val bytes = file.readBytes()
                            val base64 = Base64.encodeToString(bytes, Base64.NO_WRAP)
                            val mimeType = if (path.endsWith(".png", ignoreCase = true)) "png" else "jpeg"
                            val dataUrl = "data:image/$mimeType;base64,$base64"

                            contents.add(Content(type = "image_url", image_url = ImageUrl(dataUrl)))
                        } catch (e: Exception) { /* skip bad image */ }
                    }
                }

                // Add the text question
                contents.add(
                    Content(
                        type = "text",
                        text = """
                                            You are an expert, kind, and patient teacher.
                                            The images you see are from a student's notes, textbook pages, or study materials.
                                            Your job is to help the student learn — like a real teacher would.
                                            
                                            You can:
                                            • Explain concepts clearly and simply
                                            • Create practice quizzes with answers
                                            • Summarize key points
                                            • Point out important details in the images
                                            • Help with homework or exam prep
                                            
                                            Always be encouraging, clear, and structured.
                                            Use bullet points, numbered lists, and bold key terms when helpful.
                                            If the student asks for a quiz, make 4–6 good questions with answers at the end.
                                            Speak directly to the student — like "Great question!" or "Let me explain this step by step".
                                            
                                            Respond ONLY to the student's request. Do not repeat this prompt or add extra information.
                                        """.trimIndent()
                    )
                )

                // Send request
                val response = GrokClient.api.askGrok(
                    GrokRequest(
                        messages = listOf(
                            GrokMessage(role = "system", content = listOf(Content(type = "text", text = "You are a helpful AI teacher."))),
                            GrokMessage(role = "user", content = contents)
                        ),
                        model = "grok-2-vision-1212"
                    )
                )

                _answer.value = response.choices.firstOrNull()?.message?.content?.trim()
                    ?: "No response from Grok."

            } catch (e: Exception) {
                _errorMessage.value = "Error: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun clearError() {
        _errorMessage.value = null
    }

    fun clearAnswerAndError() {
        _answer.value = ""
        _errorMessage.value = null
    }
}