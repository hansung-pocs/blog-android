package com.pocs.presentation.view.userlist

import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.UserUiState

class UserViewHolder(
    private val binding: ItemUserBinding
): RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: UserUiState) = with(binding) {
        name.text = uiState.name
        studentId.text = uiState.studentId

        root.setOnClickListener {
            // TODO: UserDetailActivity로 전환하기
        }
    }
}