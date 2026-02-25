package com.jobsniper.ai.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jobsniper.ai.data.local.entity.JobEntity

@Database(entities = [JobEntity::class], version = 1, exportSchema = false)
abstract class JobSniperDatabase : RoomDatabase() {
    abstract fun jobDao(): JobDao
}
