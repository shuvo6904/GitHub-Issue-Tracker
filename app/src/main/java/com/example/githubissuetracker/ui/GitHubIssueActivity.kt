package com.example.githubissuetracker.ui

import com.example.githubissuetracker.utils.RecyclerViewItemDecoration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import com.example.githubissuetracker.R
import com.example.githubissuetracker.databinding.ActivityGithubIssueBinding
import com.example.githubissuetracker.utils.GenericLoadStateAdapter
import com.example.githubissuetracker.utils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import io.noties.markwon.Markwon
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GitHubIssueActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGithubIssueBinding
    private val gitHubIssueAdapter by lazy {
        val markwon = Markwon.create(this)
        GithubIssueAdapter(markwon)
    }
    private val headerAdapter by lazy {
        GenericLoadStateAdapter { gitHubIssueAdapter.retry() }
    }
    private val footerAdapter by lazy {
        GenericLoadStateAdapter { gitHubIssueAdapter.retry() }
    }
    private val viewModel: GitHubIssueViewModel by viewModels()
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
        binding.apply {
            // Set up RecyclerView adapter with header and footer
            recyclerview.adapter = gitHubIssueAdapter.withLoadStateHeaderAndFooter(
                header = headerAdapter,
                footer = footerAdapter
            )

            // Add a custom divider using com.example.githubissuetracker.utils.RecyclerViewItemDecoration
            val customDivider = RecyclerViewItemDecoration(recyclerview.context, R.drawable.divider_github_issue_rv)
            recyclerview.addItemDecoration(customDivider)

            lifecycleScope.launch {
                gitHubIssueAdapter.loadStateFlow.collectLatest { combinedLoadState ->
                    progressBar.isVisible = combinedLoadState.refresh is LoadState.Loading
                    recyclerview.isVisible = combinedLoadState.refresh is LoadState.NotLoading
                    /*binding.layoutNoProductsFound.root.isVisible =
                        combinedLoadState.refresh is LoadState.NotLoading && searchProductAdapterPaged.itemCount == 0*/

                    if (combinedLoadState.refresh is LoadState.Error) {
                        val error = (combinedLoadState.refresh as LoadState.Error).error
                        Toast.makeText(this@GitHubIssueActivity, error.message, Toast.LENGTH_LONG)
                            .show()
                        /*if (error is UnknownHostException && !isConnectedToInternet()) {
                            binding.layoutRetry.root.isVisible = false
                            supportFragmentManager.showNoInternetConnection {
                                searchProductAdapterPaged.retry()
                            }
                        } else {
                            binding.layoutRetry.root.isVisible = true
                        }*/
                    } else {
                        //binding.layoutRetry.root.isVisible = false
                    }
                }

            }
        }

    }

    private fun fetchData() {
        val queryText = binding.edtSearch.text.trim().toString()
        lifecycleScope.launch {
            viewModel.getGithubIssues(queryText).collect { pagingData ->
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
        }
    }
}