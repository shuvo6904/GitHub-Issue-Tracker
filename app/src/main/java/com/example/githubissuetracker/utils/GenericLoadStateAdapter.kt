package com.example.githubissuetracker.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.githubissuetracker.databinding.LayoutCommonLoadStateBinding

class GenericLoadStateAdapter(
    private val retryCallback: () -> Unit
) : LoadStateAdapter<GenericLoadStateAdapter.LoadStateViewHolder>() {

    override fun onBindViewHolder(
        holder: LoadStateViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ) = LoadStateViewHolder(
        LayoutCommonLoadStateBinding.inflate(LayoutInflater.from(parent.context), parent, false),
        retryCallback
    )

    class LoadStateViewHolder(
        private val binding: LayoutCommonLoadStateBinding,
        private val retryCallback: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.retryBtn.setOnClickListener { retryCallback() }
        }

        fun bind(loadState: LoadState) {
            val isLoading = loadState is LoadState.Loading
            val isError = loadState is LoadState.Error
            binding.apply {
                loadingLayout.isVisible = isLoading
                errorLayout.isVisible = isError
                if (isError) {
                    val error = (loadState as LoadState.Error).error
                    //errorMsg.text = error.message
                    //errorMsg.text = error.message
                    errorMsg.text = "error.message and no internet connection found in your phone. please try to connect internet or trun on wifi of your home"
                }
            }
            /*binding.loadingLayout.isVisible = loadState is LoadState.Loading
            binding.errorLayout.isVisible = loadState is LoadState.Error*/
        }
    }

}