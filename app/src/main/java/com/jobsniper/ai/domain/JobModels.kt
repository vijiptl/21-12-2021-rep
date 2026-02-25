package com.jobsniper.ai.domain

data class ParsedJob(
    val id: String,
    val title: String,
    val company: String,
    val location: String,
    val postedEpochMillis: Long,
    val applicantsCount: Int?,
    val jobUrl: String,
    val description: String
)

data class TailoredPackage(
    val resumeBullets: String,
    val recruiterMessage: String,
    val screeningAnswers: String,
    val coverNote: String
)
