package com.example.githubissuetracker.ui

import com.example.githubissuetracker.utils.RecyclerViewItemDecoration
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.githubissuetracker.Constants
import com.example.githubissuetracker.R
import com.example.githubissuetracker.databinding.ActivityGithubIssueBinding
import com.example.githubissuetracker.utils.GenericLoadStateAdapter
import com.example.githubissuetracker.utils.hideKeyboard
import com.example.githubissuetracker.utils.openActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GitHubIssueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGithubIssueBinding
    private var fullRepoName = ""
    private var repoName = ""
    private val gitHubIssueAdapter by lazy {
        GithubIssueAdapter { item ->
            openActivity<GitHubIssueDetailsActivity> {
                putExtra(Constants.GITHUB_ISSUES_ITEM, item)
            }
        }
    }
    private val headerAdapter by lazy {
        GenericLoadStateAdapter { gitHubIssueAdapter.retry() }
    }
    private val footerAdapter by lazy {
        GenericLoadStateAdapter { gitHubIssueAdapter.retry() }
    }
    private val viewModel: GitHubViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGithubIssueBinding.inflate(layoutInflater)
        //enableEdgeToEdge()
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
        fullRepoName = intent.getStringExtra(Constants.FULL_REPO_NAME) ?: ""
        repoName = intent.getStringExtra(Constants.REPO_NAME) ?: ""
        binding.issueListText.text = getString(R.string.issue_list, repoName)
        binding.apply {
            // Set up RecyclerView adapter with header and footer
            recyclerview.adapter = gitHubIssueAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )

            // Add a custom divider RecyclerViewItemDecoration
            val customDivider = RecyclerViewItemDecoration(recyclerview.context, R.drawable.divider_github_issue_rv)
            recyclerview.addItemDecoration(customDivider)

            lifecycleScope.launch {
                gitHubIssueAdapter.loadStateFlow.collectLatest { combinedLoadState ->
                    val refreshState = combinedLoadState.refresh
                    val isLoading = refreshState is LoadState.Loading
                    val isNotLoading = refreshState is LoadState.NotLoading
                    val isError = refreshState is LoadState.Error

                    // Handle visibility
                    progressBar.isVisible = isLoading
                    recyclerview.isVisible = isNotLoading
                    binding.noIssuesAvailable.isVisible = isNotLoading && gitHubIssueAdapter.itemCount == 0

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
        val queryText = binding.edtSearch.text.trim().toString()
        lifecycleScope.launch {
            viewModel.getGithubIssues(queryText, fullRepoName).collect { pagingData ->
                gitHubIssueAdapter.submitData(pagingData)
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
                gitHubIssueAdapter.retry()
            }

            backBtn.setOnClickListener {
                finish()
            }
        }
    }
}