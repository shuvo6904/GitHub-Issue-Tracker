package com.example.githubissuetracker.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.githubissuetracker.Constants
import com.example.githubissuetracker.ui.GitHubIssuePagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiService: GitHubApiService
) {

    fun getGitHubIssues(queryText: String) = Pager(
            config = PagingConfig(pageSize = Constants.GITHUB_ISSUES_PER_PAGE),
            pagingSourceFactory = { GitHubIssuePagingSource(apiService, queryText) }
        ).flow.flowOn(Dispatchers.IO)
}