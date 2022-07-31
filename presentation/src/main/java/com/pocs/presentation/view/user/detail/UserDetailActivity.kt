package com.pocs.presentation.view.user.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.composethemeadapter3.Mdc3Theme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserDetailActivity : AppCompatActivity() {

    private val viewModel: UserDetailViewModel by viewModels()

    companion object {
        fun getIntent(context: Context, userId: Int): Intent {
            return Intent(context, UserDetailActivity::class.java).apply {
                putExtra("userId", userId)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Mdc3Theme(this) {
                UserDetailScreen(viewModel.uiState)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val userId = intent.getIntExtra("userId", -1)
        assert(userId != -1)
        viewModel.loadUserInfo(userId)
    }
}