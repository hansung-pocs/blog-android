package com.pocs.presentation.view.user

import androidx.recyclerview.widget.RecyclerView
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.user.item.UserItemUiState
import com.pocs.presentation.view.user.detail.UserDetailActivity

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val currentUserType: UserType,
    private val onClickCard: (userId: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: UserItemUiState) = with(binding) {
        val context = root.context

        name.text = uiState.name
        subtitle.text = context.getString(
            if (uiState.isKicked) {
                R.string.kicked_user_item_subtitle
            } else {
                R.string.user_item_subtitle
            },
            uiState.studentId,
            uiState.generation.toString()
        )

        val isUnknownUser = currentUserType == UserType.UNKNOWN
        cardView.isClickable = !isUnknownUser
        if (!isUnknownUser) {
            cardView.setOnClickListener {
                onClickCard(uiState.id)
            }
        }
    }
}