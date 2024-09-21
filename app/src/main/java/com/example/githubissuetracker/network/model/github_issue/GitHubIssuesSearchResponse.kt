package com.example.githubissuetracker.network.model.github_issue


import com.google.gson.annotations.SerializedName

data class GitHubIssuesSearchResponse(
    @SerializedName("items")
    val items: List<Item>?
) {

}