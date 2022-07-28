package com.pocs.presentation.view.user

import androidx.recyclerview.widget.RecyclerView
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.UserItemUiState
import com.pocs.presentation.view.user.detail.UserDetailActivity

class UserViewHolder(
    private val binding: ItemUserBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: UserItemUiState) = with(binding) {
        name.text = uiState.name
        subtitle.text = root.context.getString(
            R.string.user_item_subtitle,
            uiState.studentId,
            uiState.generation.toString()
        )

        cardView.setOnClickListener {
            val context = binding.root.context
            val intent = UserDetailActivity.getIntent(context, uiState.id)
            context.startActivity(intent)
        }
    }
}