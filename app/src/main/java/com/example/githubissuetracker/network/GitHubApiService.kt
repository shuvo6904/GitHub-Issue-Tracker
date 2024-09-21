package com.example.githubissuetracker.network

import com.example.githubissuetracker.Constants
import com.example.githubissuetracker.network.model.github_repository.GitHubRepositoryResponse
import com.example.githubissuetracker.network.model.github_issue.GitHubIssuesSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubApiService {
    @GET(Constants.REPOSITORIES_SEARCH)
    suspend fun getGitHubRepositories(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = Constants.GITHUB_REPOSITORIES_PER_PAGE,
        @Query("page") page: Int
    ): GitHubRepositoryResponse

    @GET(Constants.ISSUES_SEARCH)
    suspend fun getGitHubIssues(
        @Query("q") query: String,
        @Query("per_page") perPage: Int = Constants.GITHUB_ISSUES_PER_PAGE,
        @Query("page") page: Int
    ): GitHubIssuesSearchResponse
}