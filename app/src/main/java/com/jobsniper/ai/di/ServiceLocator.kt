package com.jobsniper.ai.di

import android.content.Context
import androidx.room.Room
import com.jobsniper.ai.BuildConfig
import com.jobsniper.ai.data.local.JobSniperDatabase
import com.jobsniper.ai.data.network.GmailApiService
import com.jobsniper.ai.data.network.OpenAiApiService
import com.jobsniper.ai.data.repository.JobRepository
import com.jobsniper.ai.domain.JobMonitorUseCase
import com.jobsniper.ai.util.TokenStore
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ServiceLocator {
    private lateinit var appContext: Context

    private val moshi by lazy {
        Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    }

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BASIC
            })
            .build()
    }

    val tokenStore: TokenStore by lazy { TokenStore(appContext) }

    private val db by lazy {
        Room.databaseBuilder(appContext, JobSniperDatabase::class.java, "jobsniper.db").build()
    }

    private val gmailApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.GMAIL_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(GmailApiService::class.java)
    }

    private val openAiApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.OPENAI_BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(client)
            .build()
            .create(OpenAiApiService::class.java)
    }

    val jobRepository: JobRepository by lazy {
        JobRepository(
            jobDao = db.jobDao(),
            gmailApiService = gmailApi,
            openAiApiService = openAiApi,
            tokenStore = tokenStore
        )
    }

    val monitorUseCase: JobMonitorUseCase by lazy { JobMonitorUseCase(jobRepository) }

    fun init(context: Context) {
        appContext = context.applicationContext
    }
}
