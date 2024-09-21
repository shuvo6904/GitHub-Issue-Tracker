package com.example.githubissuetracker.network.model.github_issue


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    @SerializedName("body")
    val body: String? = null,
    @SerializedName("comments")
    val comments: Int? = null,
    @SerializedName("created_at")
    val createdAt: String? = null,
    @SerializedName("id")
    val id: Long? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("title")
    val title: String? = null,
    @SerializedName("user")
    val user: User? = null
): Parcelable