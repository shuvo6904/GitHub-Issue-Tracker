package com.example.githubissuetracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.githubissuetracker.network.AppRepository
import com.example.githubissuetracker.network.model.search_issue.Item
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class GitHubIssueViewModel @Inject constructor(
    private val repository: AppRepository
) : ViewModel() {

    fun getGithubIssues(queryText: String): Flow<PagingData<Item>> {
        return repository.getGitHubIssues(queryText)
            .cachedIn(viewModelScope)
    }

}