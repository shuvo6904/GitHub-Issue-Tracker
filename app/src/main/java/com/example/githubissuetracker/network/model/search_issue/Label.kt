package com.example.githubissuetracker.network.model.search_issue


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Label(
    @SerializedName("color")
    val color: String?,
    @SerializedName("default")
    val default: Boolean?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("node_id")
    val nodeId: String?,
    @SerializedName("url")
    val url: String?
): Parcelable