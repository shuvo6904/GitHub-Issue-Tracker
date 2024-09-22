package com.example.githubissuetracker.ui.github_repository

import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import com.example.githubissuetracker.utils.Constants
import com.example.githubissuetracker.R
import com.example.githubissuetracker.databinding.ActivityRepositoryBinding
import com.example.githubissuetracker.ui.github_issue.GitHubIssueActivity
import com.example.githubissuetracker.viewmodel.GitHubViewModel
import com.example.githubissuetracker.utils.GenericLoadStateAdapter
import com.example.githubissuetracker.utils.RecyclerViewItemDecoration
import com.example.githubissuetracker.utils.hideKeyboard
import com.example.githubissuetracker.utils.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RepositoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRepositoryBinding
    private val viewModel: GitHubViewModel by viewModels()

    private val gitHubRepositoryAdapter by lazy {
        GitHubRepositoryAdapter { item ->
            openActivity<GitHubIssueActivity> {
                putExtra(Constants.FULL_REPO_NAME, item.fullName ?: "")
                putExtra(Constants.REPO_NAME, item.name ?: "")
            }
        }
    }

    private val headerAdapter by lazy {
        GenericLoadStateAdapter { gitHubRepositoryAdapter.retry() }
    }
    private val footerAdapter by lazy {
        GenericLoadStateAdapter { gitHubRepositoryAdapter.retry() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        binding = ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeLoadStateFlow()
        fetchData()
        initListeners()
    }

    private fun initViews() {
        binding.apply {
            repositoryListText.text = getString(R.string.repository_list, Constants.FLUTTER)
            // Set up RecyclerView adapter with header and footer
            recyclerview.adapter = gitHubRepositoryAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )

            // Add a custom divider RecyclerViewItemDecoration
            val customDivider =
                RecyclerViewItemDecoration(recyclerview.context, R.drawable.item_divider)
            recyclerview.addItemDecoration(customDivider)
        }
    }

    private fun observeLoadStateFlow() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.RESUMED) {
                gitHubRepositoryAdapter.loadStateFlow.collectLatest { combinedLoadState ->
                    binding.apply {
                        val refreshState = combinedLoadState.refresh
                        val isLoading = refreshState is LoadState.Loading
                        val isNotLoading = refreshState is LoadState.NotLoading
                        val isError = refreshState is LoadState.Error

                        // Handle visibility
                        progressBar.isVisible = isLoading
                        recyclerview.isVisible = isNotLoading
                        noRepositoriesFound.isVisible =
                            isNotLoading && gitHubRepositoryAdapter.itemCount == 0

                        // Handle error state and retry layout visibility
                        layoutRetry.root.isVisible = isError
                        if (isError) {
                            val error = (refreshState as LoadState.Error).error
                            layoutRetry.errorText.text = error.message
                        }
                    }
                }
            }

        }
    }

    private fun fetchData() {
        var queryText = binding.edtSearch.text.trim().toString()
        if (queryText.isEmpty()) queryText = Constants.FLUTTER
        binding.repositoryListText.text = getString(R.string.repository_list, queryText)
        lifecycleScope.launch {
            viewModel.getGitHubRepository(queryText).collect { pagingData ->
                gitHubRepositoryAdapter.submitData(pagingData)
            }
        }
    }

    private fun initListeners() {
        binding.apply {
            btnSearchIcon.setOnClickListener {
                currentFocus?.let { hideKeyboard(it) }
                edtSearch.clearFocus()
                recyclerview.scrollToPosition(0)
                fetchData()
            }

            layoutRetry.btnRetry.setOnClickListener {
                gitHubRepositoryAdapter.retry()
            }

            edtSearch.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    binding.btnSearchIcon.performClick()
                    return@OnEditorActionListener true
                }
                false
            })
        }
    }
}