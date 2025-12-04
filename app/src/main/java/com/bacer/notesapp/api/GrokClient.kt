package com.bacer.notesapp.api

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object GrokClient {
    private val client = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val key = ApiKey.GROK_API_KEY.trim()

            Log.d("GROK_DEBUG", "API Key used: '$key'")

            val request = chain.request().newBuilder()
                .addHeader("Content-Type", "application/json")
                .apply {
                    if (key.isNotBlank()) {
                        addHeader("Authorization", "Bearer $key")
                        Log.d("GROK_DEBUG", "Authorization header added")
                    } else {
                        Log.e("GROK_DEBUG", "API KEY IS EMPTY! Check ApiKey.kt")
                    }
                }
                .build()

            chain.proceed(request)
        }
        .build()

    val api: GrokApiService = Retrofit.Builder()
        .baseUrl("https://api.x.ai/v1/")
        .client(client)
        .addConverterFactory(MoshiConverterFactory.create())
        .build()
        .create(GrokApiService::class.java)
}