package com.pocs.presentation.view.post.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.extension.getSerializableExtra
import com.pocs.presentation.extension.setResultRefresh
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

        WindowCompat.setDecorFitsSystemWindows(window, true)

        val category = getSerializableExtra(this, "category", PostCategory::class.java)
        viewModel.initUiState(category = category)

        setContent {
            Mdc3Theme(this) {
                PostCreateScreen(
                    uiState = viewModel.uiState.value,
                    navigateUp = ::finish,
                    onSuccessSave = {
                        setResultRefresh(R.string.post_added)
                    }
                )
            }
        }
    }
}
