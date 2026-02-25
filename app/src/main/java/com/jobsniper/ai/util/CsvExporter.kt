package com.jobsniper.ai.util

import com.jobsniper.ai.data.local.entity.JobEntity

object CsvExporter {
    fun export(jobs: List<JobEntity>): String {
        val header = "id,title,company,location,postedEpochMillis,applicantsCount,jobUrl"
        val rows = jobs.joinToString("\n") { job ->
            listOf(
                job.id,
                job.title,
                job.company,
                job.location,
                job.postedEpochMillis.toString(),
                job.applicantsCount?.toString().orEmpty(),
                job.jobUrl
            ).joinToString(",") { safe(it) }
        }
        return "$header\n$rows"
    }

    private fun safe(value: String): String = "\"${value.replace("\"", "\"\"")}\""
}
