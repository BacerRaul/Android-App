package com.bacer.notesapp.api

import retrofit2.http.Body
import retrofit2.http.POST
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GrokMessage(
    val role: String,
    val content: List<Content>
)

@JsonClass(generateAdapter = true)
data class Content(
    val type: String,
    val text: String? = null,
    val image_url: ImageUrl? = null
)

@JsonClass(generateAdapter = true)
data class ImageUrl(
    val url: String
)

@JsonClass(generateAdapter = true)
data class GrokRequest(
    val messages: List<GrokMessage>,
    val model: String = "grok-2-vision-1212"
)

@JsonClass(generateAdapter = true)
data class GrokResponse(
    val choices: List<Choice>
)

@JsonClass(generateAdapter = true)
data class Choice(
    val message: Message
)

@JsonClass(generateAdapter = true)
data class Message(
    val content: String
)

interface GrokApiService {
    @POST("chat/completions")
    suspend fun askGrok(@Body request: GrokRequest): GrokResponse

    companion object {
        const val BASE_URL = "https://api.x.ai/v1/"
    }
}