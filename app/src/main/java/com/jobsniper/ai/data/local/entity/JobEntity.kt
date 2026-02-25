package com.jobsniper.ai.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jobs")
data class JobEntity(
    @PrimaryKey val id: String,
    val title: String,
    val company: String,
    val location: String,
    val postedEpochMillis: Long,
    val applicantsCount: Int?,
    val jobUrl: String,
    val description: String,
    val tailoredBullets: String,
    val recruiterMessage: String,
    val screeningAnswers: String,
    val coverNote: String,
    val createdAtEpochMillis: Long
)
