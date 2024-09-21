package com.example.githubissuetracker.ui

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.githubissuetracker.Constants
import com.example.githubissuetracker.network.GitHubApiService
import com.example.githubissuetracker.network.model.search_issue.Item

class GitHubIssuePagingSource(
    private val apiService: GitHubApiService,
    private val queryText: String,
    private val fullRepoName: String
) : PagingSource<Int, Item>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Item> {
        val page = params.key ?: 1
        return try {
            val query = "repo:$fullRepoName\u0020type:issue\u0020$queryText"
            val response = apiService.getGitHubIssues(query = query, page = page)

            var result: ArrayList<Item>? = response.items?.let { items ->
                // Filter the items where the title does NOT contain "flutter", ignoring case
                val filteredItems = items.filter { item ->
                    item.title?.contains(Constants.FLUTTER, ignoreCase = true) != true
                }
                // Convert the filtered list to an ArrayList
                ArrayList(filteredItems)
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

    override fun getRefreshKey(state: PagingState<Int, Item>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}