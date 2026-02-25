package com.jobsniper.ai.data.network

import com.jobsniper.ai.data.network.dto.GmailMessageDetailResponse
import com.jobsniper.ai.data.network.dto.GmailMessagesResponse
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface GmailApiService {
    @GET("users/me/messages")
    suspend fun listMessages(
        @Header("Authorization") bearerToken: String,
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int = 10
    ): GmailMessagesResponse

    @GET("users/me/messages/{id}")
    suspend fun getMessage(
        @Header("Authorization") bearerToken: String,
        @Path("id") id: String,
        @Query("format") format: String = "full"
    ): GmailMessageDetailResponse
}
