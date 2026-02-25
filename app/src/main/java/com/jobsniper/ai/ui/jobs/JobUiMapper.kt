package com.jobsniper.ai.ui.jobs

import com.jobsniper.ai.data.local.entity.JobEntity

object JobUiMapper {
    fun map(entity: JobEntity, now: Long): JobItemUi {
        val mins = ((now - entity.postedEpochMillis) / 60000).coerceAtLeast(0)
        return JobItemUi(
            id = entity.id,
            title = entity.title,
            company = entity.company,
            postedAgo = "$mins min ago",
            jobUrl = entity.jobUrl,
            bullets = entity.tailoredBullets,
            recruiterMessage = entity.recruiterMessage,
            screeningAnswers = entity.screeningAnswers,
            coverNote = entity.coverNote
        )
    }
}
