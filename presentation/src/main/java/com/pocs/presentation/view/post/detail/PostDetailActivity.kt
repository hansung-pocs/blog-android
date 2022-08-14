package com.pocs.presentation.view.post.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.presentation.R
import com.pocs.presentation.extension.RefreshStateContract
import com.pocs.presentation.extension.setResultRefresh
import com.pocs.presentation.model.post.PostDetailUiState
import com.pocs.presentation.view.post.edit.PostEditActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {

    private val viewModel: PostDetailViewModel by viewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    companion object {
        fun getIntent(context: Context, id: Int): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra("id", id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        super.onCreate(savedInstanceState)

        setContent {
            Mdc3Theme(this) {
                PostDetailScreen(
                    viewModel,
                    onEditClick = ::startPostEditActivity,
                    onDeleteSuccess = ::onDeleteSuccess
                )
            }
        }

        fetchPost()

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                setResultRefresh()
                fetchPost()
                it.message?.let { message -> viewModel.showUserMessage(message) }
            }
        }
    }

    private fun onDeleteSuccess() {
        setResultRefresh(R.string.post_deleted)
        finish()
    }

    private fun fetchPost() {
        val id = intent.getIntExtra("id", -1)

        viewModel.fetchPost(id)
    }

    private fun startPostEditActivity() {
        val uiState = viewModel.uiState.value
        require(uiState is PostDetailUiState.Success)

        val postDetail = uiState.postDetail
        val intent = PostEditActivity.getIntent(
            this,
            postDetail.id,
            postDetail.title,
            postDetail.content,
            postDetail.category
        )
        launcher?.launch(intent)
    }
}