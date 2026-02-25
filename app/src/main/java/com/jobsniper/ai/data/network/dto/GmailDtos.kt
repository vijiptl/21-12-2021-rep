package com.jobsniper.ai.data.network.dto

data class GmailMessagesResponse(
    val messages: List<GmailMessageItem>?
)

data class GmailMessageItem(
    val id: String
)

data class GmailMessageDetailResponse(
    val id: String,
    val internalDate: String,
    val snippet: String,
    val payload: GmailPayload
)

data class GmailPayload(
    val headers: List<GmailHeader>?
)

data class GmailHeader(
    val name: String,
    val value: String
)
