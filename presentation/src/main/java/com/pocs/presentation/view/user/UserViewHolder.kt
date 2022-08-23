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
        val defaultInfo = uiState.defaultInfo
        if (defaultInfo != null) { // 동아리 회원인 경우
            name.text = defaultInfo.name
            subtitle.text = context.getString(
                if (uiState.isKicked) {
                    R.string.kicked_user_item_subtitle
                } else {
                    R.string.user_item_subtitle
                },
                defaultInfo.studentId,
                defaultInfo.generation.toString()
            )
        } else { // 익명 로그인 경우
            name.text = context.getString(R.string.anonymous_name, uiState.id.toString())
        }

        val isUnknownUser = currentUserType == UserType.ANONYMOUS
        cardView.isClickable = !isUnknownUser
        if (!isUnknownUser) {
            cardView.setOnClickListener {
                onClickCard(uiState.id)
            }
        }
    }
}