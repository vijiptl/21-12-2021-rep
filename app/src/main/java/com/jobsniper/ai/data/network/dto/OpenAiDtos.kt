package com.jobsniper.ai.data.network.dto

data class OpenAiRequest(
    val model: String,
    val temperature: Double,
    val messages: List<OpenAiMessage>
)

data class OpenAiMessage(
    val role: String,
    val content: String
)

data class OpenAiResponse(
    val choices: List<OpenAiChoice>
)

data class OpenAiChoice(
    val message: OpenAiMessage
)
