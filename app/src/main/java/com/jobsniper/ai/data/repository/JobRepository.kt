package com.jobsniper.ai.data.repository

import androidx.lifecycle.LiveData
import com.jobsniper.ai.data.local.JobDao
import com.jobsniper.ai.data.local.entity.JobEntity
import com.jobsniper.ai.data.network.GmailApiService
import com.jobsniper.ai.data.network.OpenAiApiService
import com.jobsniper.ai.data.network.dto.OpenAiMessage
import com.jobsniper.ai.data.network.dto.OpenAiRequest
import com.jobsniper.ai.domain.JobFilter
import com.jobsniper.ai.domain.LinkedInAlertParser
import com.jobsniper.ai.domain.TailoredPackage
import com.jobsniper.ai.util.ProfileConfig
import com.jobsniper.ai.util.TokenStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JobRepository(
    private val jobDao: JobDao,
    private val gmailApiService: GmailApiService,
    private val openAiApiService: OpenAiApiService,
    private val tokenStore: TokenStore
) {
    fun observeJobs(): LiveData<List<JobEntity>> = jobDao.observeJobs()

    suspend fun fetchAndStoreEligibleJobs(): Int = withContext(Dispatchers.IO) {
        val gmailToken = tokenStore.gmailToken()
        val openAiToken = tokenStore.openAiToken()
        if (gmailToken.isBlank() || openAiToken.isBlank()) return@withContext 0

        val existingIds = jobDao.getExistingIds().toSet()
        val query = "from:jobs-noreply@linkedin.com newer_than:1d"
        val messageIds = gmailApiService
            .listMessages("Bearer $gmailToken", query)
            .messages
            .orEmpty()
            .map { it.id }
            .filterNot { it in existingIds }

        var inserted = 0
        messageIds.forEach { id ->
            val detail = gmailApiService.getMessage("Bearer $gmailToken", id)
            val parsed = LinkedInAlertParser.parse(detail) ?: return@forEach
            if (!JobFilter.isEligible(parsed, System.currentTimeMillis())) return@forEach

            val tailored = generateTailoredContent(openAiToken, parsed.description)

            jobDao.upsert(
                JobEntity(
                    id = parsed.id,
                    title = parsed.title,
                    company = parsed.company,
                    location = parsed.location,
                    postedEpochMillis = parsed.postedEpochMillis,
                    applicantsCount = parsed.applicantsCount,
                    jobUrl = parsed.jobUrl,
                    description = parsed.description,
                    tailoredBullets = tailored.resumeBullets,
                    recruiterMessage = tailored.recruiterMessage,
                    screeningAnswers = tailored.screeningAnswers,
                    coverNote = tailored.coverNote,
                    createdAtEpochMillis = System.currentTimeMillis()
                )
            )
            inserted += 1
        }
        inserted
    }

    suspend fun markOpened(jobId: String) = withContext(Dispatchers.IO) {
        jobDao.markOpened(jobId, System.currentTimeMillis())
    }

    private suspend fun generateTailoredContent(apiKey: String, description: String): TailoredPackage {
        val system = """
            You are a career assistant. Generate concise outputs for fast manual LinkedIn applications.
            Return markdown with sections: BULLETS, MESSAGE, SCREENING, COVER_NOTE.
            Keep total response under 250 words.
        """.trimIndent()

        val user = """
            Candidate: ${ProfileConfig.CANDIDATE_NAME}, ${ProfileConfig.EXPERIENCE}
            Skills: ${ProfileConfig.SKILLS}
            Target roles: ${ProfileConfig.TARGET_ROLES}
            Location: ${ProfileConfig.TARGET_LOCATION}
            Preferred domains: ${ProfileConfig.TARGET_DOMAINS}

            Job Description:
            $description

            Need:
            1) 4 tailored resume bullets.
            2) 3-line recruiter message.
            3) Screening question answers.
            4) 150-word cover note.
        """.trimIndent()

        val response = openAiApiService.generate(
            bearerToken = "Bearer $apiKey",
            request = OpenAiRequest(
                model = "gpt-4o-mini",
                temperature = 0.2,
                messages = listOf(
                    OpenAiMessage("system", system),
                    OpenAiMessage("user", user)
                )
            )
        )

        val content = response.choices.firstOrNull()?.message?.content.orEmpty()
        return parseTailoredContent(content)
    }

    private fun parseTailoredContent(content: String): TailoredPackage {
        fun extract(section: String): String {
            val regex = Regex("$section:?\\n([\\s\\S]*?)(\\n[A-Z_]+:|$)")
            return regex.find(content)?.groupValues?.get(1)?.trim().orEmpty()
        }
        return TailoredPackage(
            resumeBullets = extract("BULLETS"),
            recruiterMessage = extract("MESSAGE"),
            screeningAnswers = extract("SCREENING"),
            coverNote = extract("COVER_NOTE")
        )
    }
}
