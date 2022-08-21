package com.pocs.presentation.view.user

import androidx.recyclerview.widget.RecyclerView
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.user.item.UserItemUiState

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

        val isUnknownUser = currentUserType == UserType.비회원
        //TODO 백엔드에서 바꾸면 바꾸기
        cardView.isClickable = !isUnknownUser
        if (!isUnknownUser) {
            cardView.setOnClickListener {
                onClickCard(uiState.id)
            }
        }
    }
}