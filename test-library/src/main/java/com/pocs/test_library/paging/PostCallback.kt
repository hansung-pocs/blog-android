package com.pocs.test_library.paging

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.pocs.presentation.model.post.item.PostItemUiState

class NoopListCallback : ListUpdateCallback {
    override fun onChanged(position: Int, count: Int, payload: Any?) {}
    override fun onMoved(fromPosition: Int, toPosition: Int) {}
    override fun onInserted(position: Int, count: Int) {}
    override fun onRemoved(position: Int, count: Int) {}
}

class PostItemUiStateDiffCallback : DiffUtil.ItemCallback<PostItemUiState>() {
    override fun areItemsTheSame(oldItem: PostItemUiState, newItem: PostItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: PostItemUiState, newItem: PostItemUiState): Boolean {
        return oldItem == newItem
    }
}