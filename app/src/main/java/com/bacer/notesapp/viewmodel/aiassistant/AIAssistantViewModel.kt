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

                // Explicitly pass user question
                contents.add(
                    Content(
                        type = "text",
                        text = "USER REQUEST: $question"
                    )
                )

                // Send request
                val response = GrokClient.api.askGrok(
                    GrokRequest(
                        messages = listOf(
                            // STRICT SYSTEM MESSAGE
                            GrokMessage(
                                role = "system",
                                content = listOf(
                                    Content(
                                        type = "text",
                                        text = """
                                            You are a strict, rule-following AI teacher.

                                            The user's message may ask for:
                                            1. Explanation only
                                            2. Quiz only
                                            3. Both explanation and quiz

                                            RULES YOU MUST FOLLOW:
                                            - If the user asks for explanation → give ONLY explanation.
                                            - If the user asks for quiz → give ONLY a quiz (10 Q + answers).
                                            - If the user asks for both → give BOTH in this order:
                                                1. Explanation
                                                2. Quiz (10 Q + answers)

                                            NO extra text.
                                            NO unsolicited content.
                                            NO mixing modes.
                                            NO meta-text.
                                            NO apologies or disclaimers.

                                            Images, if provided, are student notes or materials and should be used only as context.
                                        """.trimIndent()
                                    )
                                )
                            ),

                            // USER CONTENT
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