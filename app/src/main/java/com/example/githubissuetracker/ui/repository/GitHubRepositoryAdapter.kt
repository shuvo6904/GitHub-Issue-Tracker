package com.example.githubissuetracker.ui.repository

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubissuetracker.R
import com.example.githubissuetracker.databinding.ItemGithubRepositoroyBinding
import com.example.githubissuetracker.network.model.github_repository.GitHubRepositoryResponse
import com.example.githubissuetracker.utils.loadImage

class GitHubRepositoryAdapter(
    private val onItemClicked: (GitHubRepositoryResponse.Item) -> Unit
): PagingDataAdapter<GitHubRepositoryResponse.Item, GitHubRepositoryAdapter.GitHubRepositoryViewHolder>(DIFF_CALLBACK) {
    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GitHubRepositoryResponse.Item>() {
            override fun areItemsTheSame(oldItem: GitHubRepositoryResponse.Item, newItem: GitHubRepositoryResponse.Item): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GitHubRepositoryResponse.Item, newItem: GitHubRepositoryResponse.Item): Boolean {
                return oldItem == newItem
            }

        }
    }

    class GitHubRepositoryViewHolder private constructor(
        private val binding: ItemGithubRepositoroyBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(
            item: GitHubRepositoryResponse.Item?,
            onItemClicked: (GitHubRepositoryResponse.Item) -> Unit
        ) {
            binding.apply {
                item?.let {
                    profileImage.loadImage(item.owner?.avatarUrl, R.drawable.ic_profile)
                    fullRepoName.text = item.fullName ?: ""
                    name.text = item.owner?.login ?: ""
                }

                root.setOnClickListener {
                    item?.let { onItemClicked(it) }
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): GitHubRepositoryViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGithubRepositoroyBinding.inflate(layoutInflater, parent, false)
                return GitHubRepositoryViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: GitHubRepositoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, onItemClicked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GitHubRepositoryViewHolder {
        return GitHubRepositoryViewHolder.from(parent)
    }
}