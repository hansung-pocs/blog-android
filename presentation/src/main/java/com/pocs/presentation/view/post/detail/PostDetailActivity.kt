package com.pocs.presentation.view.post.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ActivityPostDetailBinding
import com.pocs.presentation.model.PostDetailUiState
import com.pocs.presentation.view.post.edit.PostEditActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {

    private var _binding: ActivityPostDetailBinding? = null
    private val binding: ActivityPostDetailBinding get() = requireNotNull(_binding)

    private val viewModel: PostDetailViewModel by viewModels()

    companion object {
        fun getIntent(context: Context, id: Int): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra("id", id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityPostDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadArticle()
        initToolBar()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_post_detail, menu)
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val uiState = viewModel.uiState.value
        val isSuccess = uiState is PostDetailUiState.Success
        // TODO: 작성자인 경우에만 보이도록 하기
        menu.children.forEach { it.isVisible = isSuccess }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_post -> {
                startPostEditActivity()
                true
            }
            R.id.action_delete_post -> {
                // TODO: 삭제 API 연동 후 구현하기
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadArticle() {
        val id = intent.getIntExtra("id", -1)
        viewModel.loadArticle(id)
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun updateUi(uiState: PostDetailUiState) = with(binding) {
        supportActionBar?.invalidateOptionsMenu()
        progressBar.isVisible = uiState is PostDetailUiState.Loading
        when (uiState) {
            is PostDetailUiState.Success -> {
                title.text = uiState.title
                subtitle.text = getString(R.string.article_subtitle, uiState.date, uiState.writer)
                content.text = uiState.content
            }
            is PostDetailUiState.Failure -> {
                title.text = getString(R.string.failed_to_load)
                content.text = uiState.message
            }
            else -> {}
        }
    }

    private fun startPostEditActivity() {
        val uiState = viewModel.uiState.value
        if (uiState !is PostDetailUiState.Success) return

        val intent = PostEditActivity.getIntent(
            this,
            uiState.id,
            uiState.title,
            uiState.content,
            uiState.category
        )
        startActivity(intent)
    }
}