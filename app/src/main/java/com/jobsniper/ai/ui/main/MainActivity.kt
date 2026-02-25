package com.jobsniper.ai.ui.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.jobsniper.ai.databinding.ActivityMainBinding
import com.jobsniper.ai.di.ServiceLocator
import com.jobsniper.ai.ui.jobs.JobAdapter
import com.jobsniper.ai.util.NotificationHelper
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NotificationHelper.ensureChannel(this)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.gmailTokenInput.setText(ServiceLocator.tokenStore.gmailToken())
        binding.openAiTokenInput.setText(ServiceLocator.tokenStore.openAiToken())
        binding.saveTokens.setOnClickListener {
            ServiceLocator.tokenStore.saveGmailToken(binding.gmailTokenInput.text?.toString().orEmpty())
            ServiceLocator.tokenStore.saveOpenAiToken(binding.openAiTokenInput.text?.toString().orEmpty())
            Snackbar.make(binding.root, "Tokens saved securely", Snackbar.LENGTH_SHORT).show()
        }

        val adapter = JobAdapter(onOpenClicked = viewModel::markOpened)
        binding.jobList.layoutManager = LinearLayoutManager(this)
        binding.jobList.adapter = adapter

        viewModel.mappedJobs.observe(this) { jobs ->
            binding.emptyState.isVisible = jobs.isEmpty()
            adapter.submitList(jobs)
        }
    }
}
