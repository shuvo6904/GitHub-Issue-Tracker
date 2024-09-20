package com.example.githubissuetracker.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubissuetracker.R
import com.example.githubissuetracker.databinding.ItemGithubIssueBinding
import com.example.githubissuetracker.network.model.search_issue.Item
import com.example.githubissuetracker.utils.formatToDate
import com.example.githubissuetracker.utils.loadImage
import io.noties.markwon.Markwon

class GithubIssueAdapter(private val markwon: Markwon) :
    PagingDataAdapter<Item, GithubIssueAdapter.GitHubIssueViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Item>() {
            override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
                return oldItem == newItem
            }

        }
    }

    class GitHubIssueViewHolder private constructor(
        private val binding: ItemGithubIssueBinding,
        private val markwon: Markwon
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item?) {
            binding.apply {
                item?.let {
                    profileImage.loadImage(item.user?.avatarUrl, R.drawable.ic_profile)
                    title.text = item.title ?: ""
                    date.text = item.createdAt?.formatToDate() ?: ""
                    name.text = item.user?.login ?: ""
                    markwon.setMarkdown(descriptionMarkdown, item.body ?: "")
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup, markwon: Markwon): GitHubIssueViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGithubIssueBinding.inflate(layoutInflater, parent, false)
                return GitHubIssueViewHolder(binding, markwon)
            }
        }
    }

    override fun onBindViewHolder(holder: GitHubIssueViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubIssueViewHolder {
        return GitHubIssueViewHolder.from(parent, markwon)
    }
}