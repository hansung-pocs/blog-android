package com.pocs.presentation.view.user

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.user.item.UserItemUiState

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val onClickCard: (userId: Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiState: UserItemUiState) = with(binding) {
        val context = root.context
        val defaultInfo = uiState.defaultInfo
        if (defaultInfo != null) { // 동아리 회원인 경우
            name.text = defaultInfo.name
            subtitle.isVisible = true
            subtitle.text = context.getString(
                if (uiState.isKicked) {
                    R.string.kicked_user_item_subtitle
                } else {
                    R.string.user_item_subtitle
                },
                defaultInfo.studentId,
                defaultInfo.generation.toString()
            )
            Glide.with(binding.root)
                .load(defaultInfo.profileImageUrl)
                .circleCrop()
                .into(profileImage)
        } else { // 익명 로그인 경우
            name.text = context.getString(R.string.anonymous_name, uiState.id.toString())
            if (uiState.isKicked) {
                subtitle.text = context.getString(R.string.is_kicked)
                subtitle.isVisible = true
            } else {
                subtitle.text = null
                subtitle.isVisible = false
            }
            profileImage.setImageDrawable(
                AppCompatResources.getDrawable(
                    binding.root.context,
                    R.drawable.ic_baseline_account_circle_24
                )
            )
        }

        cardView.setOnClickListener { onClickCard(uiState.id) }
    }
}