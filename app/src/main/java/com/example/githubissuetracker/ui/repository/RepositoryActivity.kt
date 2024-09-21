package com.example.githubissuetracker.ui.repository

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.githubissuetracker.Constants
import com.example.githubissuetracker.R
import com.example.githubissuetracker.databinding.ActivityRepositoryBinding
import com.example.githubissuetracker.ui.GitHubIssueActivity
import com.example.githubissuetracker.ui.GitHubViewModel
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
        //enableEdgeToEdge()
        binding = ActivityRepositoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        /*ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/

        initViews()
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
            val customDivider = RecyclerViewItemDecoration(recyclerview.context, R.drawable.divider_github_issue_rv)
            recyclerview.addItemDecoration(customDivider)

            lifecycleScope.launch {
                gitHubRepositoryAdapter.loadStateFlow.collectLatest { combinedLoadState ->
                    val refreshState = combinedLoadState.refresh
                    val isLoading = refreshState is LoadState.Loading
                    val isNotLoading = refreshState is LoadState.NotLoading
                    val isError = refreshState is LoadState.Error

                    // Handle visibility
                    progressBar.isVisible = isLoading
                    recyclerview.isVisible = isNotLoading
                    binding.noRepositoriesFound.isVisible = isNotLoading && gitHubRepositoryAdapter.itemCount == 0

                    // Handle error state and retry layout visibility
                    binding.layoutRetry.root.isVisible = isError
                    if (isError) {
                        val error = (refreshState as LoadState.Error).error
                        binding.layoutRetry.errorText.text = error.message
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
                recyclerview.scrollToPosition(0)
                fetchData()
            }

            layoutRetry.btnRetry.setOnClickListener {
                gitHubRepositoryAdapter.retry()
            }
        }
    }
}