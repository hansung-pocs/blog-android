package com.pocs.presentation.paging

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemLoadStateBinding

class PagingLoadStateAdapter(
    private val retry: () -> Unit,
) : LoadStateAdapter<PagingLoadStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): PagingLoadStateViewHolder = PagingLoadStateViewHolder(parent, retry)

    override fun onBindViewHolder(holder: PagingLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }
}

class PagingLoadStateViewHolder(
    parent: ViewGroup,
    retry: () -> Unit
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context).inflate(R.layout.item_load_state, parent, false)
) {
    private val binding = ItemLoadStateBinding.bind(itemView)
    private val progressBar: ProgressBar = binding.progressBar
    private val errorMsg: TextView = binding.errorMsg
    private val retry: Button = binding.retryButton.also {
        it.setOnClickListener { retry() }
    }

    fun bind(loadState: LoadState) {
        progressBar.isVisible = loadState is LoadState.Loading
        retry.isVisible = loadState is LoadState.Error
        errorMsg.isVisible = loadState is LoadState.Error
    }
}
