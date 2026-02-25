package com.jobsniper.ai.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jobsniper.ai.data.local.entity.JobEntity
import com.jobsniper.ai.data.repository.JobRepository
import com.jobsniper.ai.di.ServiceLocator
import com.jobsniper.ai.ui.jobs.JobItemUi
import com.jobsniper.ai.ui.jobs.JobUiMapper
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: JobRepository = ServiceLocator.jobRepository
) : ViewModel() {

    val jobs: LiveData<List<JobEntity>> = repository.observeJobs()

    val mappedJobs = MediatorLiveData<List<JobItemUi>>().apply {
        addSource(jobs) { value = it.map { entity -> JobUiMapper.map(entity, System.currentTimeMillis()) } }
    }

    fun markOpened(jobId: String) {
        viewModelScope.launch { repository.markOpened(jobId) }
    }
}
