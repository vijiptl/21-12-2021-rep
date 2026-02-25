package com.jobsniper.ai.domain

object JobFilter {
    private val titleKeywords = listOf("devops", "sre", "cloud", "kubernetes")

    fun isEligible(job: ParsedJob, nowEpochMillis: Long): Boolean {
        val recent = nowEpochMillis - job.postedEpochMillis <= 10 * 60 * 1000
        val locationOk = job.location.contains("bangalore", ignoreCase = true)
        val keywordOk = titleKeywords.any { keyword ->
            job.title.contains(keyword, ignoreCase = true) || job.description.contains(keyword, ignoreCase = true)
        }
        val applicantOk = job.applicantsCount?.let { it < 100 } ?: true
        return recent && locationOk && keywordOk && applicantOk
    }
}
