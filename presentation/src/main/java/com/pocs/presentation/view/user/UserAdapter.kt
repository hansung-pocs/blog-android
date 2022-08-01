package com.pocs.presentation.view.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.user.item.UserItemUiState

class UserAdapter(
    private val currentUserType: UserType
) : PagingDataAdapter<UserItemUiState, UserViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, currentUserType = currentUserType)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    companion object {
        private val diffCallback = object : DiffUtil.ItemCallback<UserItemUiState>() {
            override fun areItemsTheSame(
                oldItem: UserItemUiState,
                newItem: UserItemUiState
            ): Boolean {
                // TODO: uid가 추가되면 그걸로 수정하기
                return oldItem.studentId == newItem.studentId
            }

            override fun areContentsTheSame(
                oldItem: UserItemUiState,
                newItem: UserItemUiState
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}