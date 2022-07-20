package com.pocs.presentation.view.userlist

import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.UserItemUiState

class UserViewHolder(
    private val binding: ItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: UserItemUiState) = with(binding) {
        name.text = uiState.name
        subtitle.text = root.context.getString(
            R.string.user_item_subtitle,
            uiState.studentId,
            uiState.generation
        )

        root.setOnClickListener {
            // TODO: UserDetailActivity로 전환하기
        }
    }
}