package com.pocs.presentation.view.user

import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ItemUserBinding
import com.pocs.presentation.model.user.item.UserItemUiState
import com.pocs.presentation.view.user.detail.UserDetailActivity
import com.pocs.presentation.view.user.detail.UserPostListActivity

class UserViewHolder(
    private val binding: ItemUserBinding,
    private val currentUserType: UserType
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
                val intent = UserDetailActivity.getIntent(context, uiState.id)
                context.startActivity(intent)
            }
        }

        if (currentUserType == UserType.ADMIN) {
            moreInfoButton.isVisible = true
            moreInfoButton.setOnClickListener { showPopup(it) }
        }
    }

    private fun showPopup(v: View) {
        val context = binding.root.context

        PopupMenu(context, v).apply {
            inflate(R.menu.menu_admin_user)
            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.admin_user_written -> {
                        val intent = UserPostListActivity.getIntent(context)
                        //TODO : User_ID 정보 넘기기 intent.putExtra("userdata", )
                        context.startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }.show()
    }
}