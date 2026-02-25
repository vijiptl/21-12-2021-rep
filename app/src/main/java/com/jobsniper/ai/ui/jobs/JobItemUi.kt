package com.jobsniper.ai.ui.jobs

data class JobItemUi(
    val id: String,
    val title: String,
    val company: String,
    val postedAgo: String,
    val jobUrl: String,
    val bullets: String,
    val recruiterMessage: String,
    val screeningAnswers: String,
    val coverNote: String
)
