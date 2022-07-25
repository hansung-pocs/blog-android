package com.pocs.presentation.view.post.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.domain.model.PostCategory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostCreateActivity : AppCompatActivity() {

    private val viewModel: PostCreateViewModel by viewModels()

    companion object {
        fun getIntent(context: Context, category: PostCategory): Intent {
            return Intent(context, PostCreateActivity::class.java).apply {
                putExtra("category", category)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val category = intent.getSerializableExtra("category") as PostCategory
        viewModel.initUiState(category = category)

        setContent {
            Mdc3Theme(this) {
                PostCreateScreen(uiState = viewModel.uiState.value)
            }
        }
    }
}