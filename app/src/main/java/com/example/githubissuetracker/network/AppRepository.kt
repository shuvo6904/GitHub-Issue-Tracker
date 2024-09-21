package com.example.githubissuetracker.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.example.githubissuetracker.utils.Constants
import com.example.githubissuetracker.ui.github_issue.GitHubIssuePagingSource
import com.example.githubissuetracker.ui.github_repository.GitHubRepositoryPagingSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class AppRepository @Inject constructor(
    private val apiService: GitHubApiService
) {

    fun getGitHubRepositories(queryText: String) = Pager(
        config = PagingConfig(pageSize = Constants.GITHUB_REPOSITORIES_PER_PAGE),
        pagingSourceFactory = { GitHubRepositoryPagingSource(apiService, queryText) }
    ).flow.flowOn(Dispatchers.IO)

    fun getGitHubIssues(queryText: String, fullRepoName: String) = Pager(
            config = PagingConfig(pageSize = Constants.GITHUB_ISSUES_PER_PAGE),
            pagingSourceFactory = { GitHubIssuePagingSource(apiService, queryText, fullRepoName) }
        ).flow.flowOn(Dispatchers.IO)

}