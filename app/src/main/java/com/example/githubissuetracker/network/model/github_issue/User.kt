package com.example.githubissuetracker.network.model.github_issue


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("avatar_url")
    val avatarUrl: String? = null,
    @SerializedName("login")
    val login: String? = null
) : Parcelable