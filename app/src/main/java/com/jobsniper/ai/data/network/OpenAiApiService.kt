package com.jobsniper.ai.data.network

import com.jobsniper.ai.data.network.dto.OpenAiRequest
import com.jobsniper.ai.data.network.dto.OpenAiResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface OpenAiApiService {
    @POST("v1/chat/completions")
    suspend fun generate(
        @Header("Authorization") bearerToken: String,
        @Body request: OpenAiRequest
    ): OpenAiResponse
}
