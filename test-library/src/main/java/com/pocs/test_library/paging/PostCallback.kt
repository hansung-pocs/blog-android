package com.pocs.test_library.paging

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.model.user.item.UserItemUiState

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

class UserItemUiStateDiffCallback : DiffUtil.ItemCallback<UserItemUiState>() {
    override fun areItemsTheSame(oldItem: UserItemUiState, newItem: UserItemUiState): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: UserItemUiState, newItem: UserItemUiState): Boolean {
        return oldItem == newItem
    }
}