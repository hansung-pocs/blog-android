package com.pocs.presentation.view.user

import android.content.Context
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
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
            uiState.generation.toString()
        )

        root.setOnClickListener {
            // TODO: UserDetailActivity로 전환하기
        }

        button.setOnClickListener {
            showPopup(it)
        }
    }

    private fun showPopup(v: View) {
        PopupMenu(binding.root.context, v).apply {
            inflate(R.menu.menu_admin_user)
            setOnMenuItemClickListener {
                when (it.itemId) {

                    else -> false
                }
            }
        }.show()
    }
}