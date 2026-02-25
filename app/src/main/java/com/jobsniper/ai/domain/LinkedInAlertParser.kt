package com.jobsniper.ai.domain

import com.jobsniper.ai.data.network.dto.GmailHeader
import com.jobsniper.ai.data.network.dto.GmailMessageDetailResponse

object LinkedInAlertParser {
    fun parse(detail: GmailMessageDetailResponse): ParsedJob? {
        val headers = detail.payload.headers.orEmpty()
        val subject = headers.findValue("Subject") ?: return null
        if (!subject.contains("LinkedIn", ignoreCase = true)) return null

        val company = headers.findValue("From")?.substringBefore("<")?.trim().orEmpty().ifBlank { "Unknown Company" }
        val title = subject.substringAfter("for", "DevOps Engineer").trim()
        val location = headers.findValue("X-LinkedIn-Location") ?: "Bangalore"
        val link = headers.findValue("X-LinkedIn-Job-Url") ?: "https://www.linkedin.com/jobs/view/${detail.id}"
        val applicants = headers.findValue("X-LinkedIn-Applicants")?.toIntOrNull()
        val description = detail.snippet
        val posted = detail.internalDate.toLongOrNull() ?: System.currentTimeMillis()

        return ParsedJob(
            id = detail.id,
            title = title,
            company = company,
            location = location,
            postedEpochMillis = posted,
            applicantsCount = applicants,
            jobUrl = link,
            description = description
        )
    }

    private fun List<GmailHeader>.findValue(name: String): String? =
        firstOrNull { it.name.equals(name, ignoreCase = true) }?.value
}
