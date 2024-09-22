package com.example.githubissuetracker.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubissuetracker.network.AppRepository
import com.example.githubissuetracker.network.model.github_repository.GitHubRepositoryResponse
import com.example.githubissuetracker.network.model.github_issue.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
class GitHubViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    fun getGitHubRepository(queryText: String): Flow<PagingData<GitHubRepositoryResponse.Item>> {
        return repository.getGitHubRepositories(queryText)
            .cachedIn(viewModelScope)
            .distinctUntilChanged()
    }

    fun getGithubIssues(queryText: String, fullRepoName: String): Flow<PagingData<Item>> {
        return repository.getGitHubIssues(queryText, fullRepoName)
            .cachedIn(viewModelScope)
            .distinctUntilChanged()
    }

}