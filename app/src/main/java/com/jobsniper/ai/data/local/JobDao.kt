package com.jobsniper.ai.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jobsniper.ai.data.local.entity.JobEntity

@Dao
interface JobDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(job: JobEntity)

    @Query("SELECT * FROM jobs ORDER BY postedEpochMillis DESC")
    fun observeJobs(): LiveData<List<JobEntity>>

    @Query("SELECT id FROM jobs")
    suspend fun getExistingIds(): List<String>

    @Query("UPDATE jobs SET createdAtEpochMillis = :timestamp WHERE id = :jobId")
    suspend fun markOpened(jobId: String, timestamp: Long)
}
