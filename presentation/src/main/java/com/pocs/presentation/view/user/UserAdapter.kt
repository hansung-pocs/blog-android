package com.pocs.presentation.view.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.user.item.UserItemUiState

class UserAdapter(
    private val currentUserType: UserType,
    private val onClickCard: (id: Int) -> Unit
) : PagingDataAdapter<UserItemUiState, UserViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(layoutInflater, parent, false)
        return UserViewHolder(binding, currentUserType = currentUserType, onClickCard = onClickCard)
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
                return oldItem.id == newItem.id
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