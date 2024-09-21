package com.example.githubissuetracker.ui.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubissuetracker.network.GitHubApiService
import com.example.githubissuetracker.network.model.github_repository.GitHubRepositoryResponse


class GitHubRepositoryPagingSource(
    private val apiService: GitHubApiService,
    private val queryText: String
): PagingSource<Int, GitHubRepositoryResponse.Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GitHubRepositoryResponse.Item> {
        val page = params.key ?: 1
        return try {
            val response = apiService.getGitHubRepositories(query = queryText, page = page)

            var result: ArrayList<GitHubRepositoryResponse.Item>? = response.items?.let { items ->
                ArrayList(items)
            }

            if (result == null) result = ArrayList()
            run {
                val nextKey = if (result.isEmpty()) {
                    null
                } else {
                    page + 1
                }

                LoadResult.Page(
                    data = result,
                    prevKey = if (page == 1) null else page - 1,
                    nextKey = nextKey
                )
            }
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GitHubRepositoryResponse.Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}