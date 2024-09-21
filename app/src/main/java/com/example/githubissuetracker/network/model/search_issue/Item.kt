package com.example.githubissuetracker.network.model.search_issue


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(
    @SerializedName("assignee")
    val assignee: Assignee?,
    @SerializedName("assignees")
    val assignees: List<Assignee?>?,
    @SerializedName("author_association")
    val authorAssociation: String?,
    @SerializedName("body")
    val body: String?,
    @SerializedName("closed_at")
    val closedAt: String?,
    @SerializedName("comments")
    val comments: Int?,
    @SerializedName("comments_url")
    val commentsUrl: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("events_url")
    val eventsUrl: String?,
    @SerializedName("html_url")
    val htmlUrl: String?,
    @SerializedName("id")
    val id: Long?,
    @SerializedName("labels")
    val labels: List<Label?>?,
    @SerializedName("labels_url")
    val labelsUrl: String?,
    @SerializedName("locked")
    val locked: Boolean?,
    @SerializedName("node_id")
    val nodeId: String?,
    @SerializedName("number")
    val number: Int?,
    @SerializedName("repository_url")
    val repositoryUrl: String?,
    @SerializedName("score")
    val score: Double?,
    @SerializedName("state")
    val state: String?,
    @SerializedName("state_reason")
    val stateReason: String?,
    @SerializedName("timeline_url")
    val timelineUrl: String?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("user")
    val user: User?
): Parcelable