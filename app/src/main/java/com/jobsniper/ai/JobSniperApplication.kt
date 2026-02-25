package com.jobsniper.ai

import android.app.Application
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.jobsniper.ai.di.ServiceLocator
import com.jobsniper.ai.worker.JobMonitorWorker
import java.util.concurrent.TimeUnit

class JobSniperApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        ServiceLocator.init(this)
        scheduleMonitoringWorker()
    }

    private fun scheduleMonitoringWorker() {
        val initial = OneTimeWorkRequestBuilder<JobMonitorWorker>()
            .setInitialDelay(15, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniqueWork(JobMonitorWorker.UNIQUE_NAME, ExistingWorkPolicy.REPLACE, initial)
    }
}
