package com.jobsniper.ai.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.jobsniper.ai.di.ServiceLocator
import com.jobsniper.ai.util.NotificationHelper
import java.util.concurrent.TimeUnit

class JobMonitorWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        return runCatching {
            val newJobs = ServiceLocator.monitorUseCase.execute()
            if (newJobs > 0) NotificationHelper.notify(
                applicationContext,
                title = "New DevOps Job - Apply Now",
                message = "Tailored content is ready. Open JobSniper AI."
            )
            scheduleNext(applicationContext)
            Result.success()
        }.getOrElse {
            Result.retry()
        }
    }

    private fun scheduleNext(context: Context) {
        val next = OneTimeWorkRequestBuilder<JobMonitorWorker>()
            .setInitialDelay(2, TimeUnit.MINUTES)
            .build()
        WorkManager.getInstance(context)
            .beginUniqueWork(UNIQUE_NAME, ExistingWorkPolicy.APPEND_OR_REPLACE, next)
            .enqueue()
    }

    companion object {
        const val UNIQUE_NAME = "job-monitor"
    }
}
