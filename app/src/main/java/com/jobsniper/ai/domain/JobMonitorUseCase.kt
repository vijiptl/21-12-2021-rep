package com.jobsniper.ai.domain

import com.jobsniper.ai.data.repository.JobRepository

class JobMonitorUseCase(
    private val repository: JobRepository
) {
    suspend fun execute(): Int = repository.fetchAndStoreEligibleJobs()
}
