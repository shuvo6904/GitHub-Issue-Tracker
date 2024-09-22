package com.example.githubissuetracker.network.model.github_repository


import com.google.gson.annotations.SerializedName

data class GitHubRepositoryResponse(
    @SerializedName("items")
    val items: List<Item>? = null
) {
    data class Item(
        @SerializedName("id")
        val id: Int? = null,
        @SerializedName("name")
        val name: String? = null,
        @SerializedName("full_name")
        val fullName: String? = null,
        @SerializedName("owner")
        val owner: Owner? = null
    ) {
        data class Owner(
            @SerializedName("login")
            val login: String? = null,
            @SerializedName("avatar_url")
            val avatarUrl: String? = null
        )
    }
}