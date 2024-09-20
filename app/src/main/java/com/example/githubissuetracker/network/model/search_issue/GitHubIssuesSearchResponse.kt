package com.example.githubissuetracker.network.model.search_issue


import com.google.gson.annotations.SerializedName

data class GitHubIssuesSearchResponse(
    @SerializedName("incomplete_results")
    val incompleteResults: Boolean?,
    @SerializedName("items")
    val items: List<Item>?,
    @SerializedName("total_count")
    val totalCount: Int?
)