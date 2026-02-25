package com.jobsniper.ai.domain

data class ReferralEntry(
    val jobId: String,
    val referrerName: String,
    val referrerContact: String,
    val notes: String
)

enum class ApplicationStatus {
    NEW,
    APPLIED,
    INTERVIEW,
    OFFER,
    REJECTED
}
