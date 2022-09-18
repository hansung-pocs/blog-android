package com.pocs.presentation.view.post.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.extension.getSerializableExtra
import com.pocs.presentation.extension.setResultRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostEditActivity : AppCompatActivity() {

    private val viewModel: PostEditViewModel by viewModels()

    companion object {
        fun getIntent(
            context: Context,
            id: Int,
            title: String,
            content: String,
            category: PostCategory,
            onlyMember: Boolean,
        ): Intent {
            return Intent(context, PostEditActivity::class.java).apply {
                putExtra("id", id)
                putExtra("title", title)
                putExtra("content", content)
                putExtra("category", category)
                putExtra("isMember", onlyMember)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        viewModel.initUiState(
            id = intent.getIntExtra("id", -1),
            title = intent.getStringExtra("title")!!,
            content = intent.getStringExtra("content")!!,
            category = getSerializableExtra(this, "category", PostCategory::class.java),
            onlyMember = intent.getBooleanExtra("isMember", true)
        )

        setContent {
            Mdc3Theme(this) {
                PostEditScreen(
                    uiState = viewModel.uiState.value,
                    navigateUp = ::finish,
                    onSuccessSave = {
                        setResultRefresh(R.string.post_edited)
                    }
                )
            }
        }
    }
}
