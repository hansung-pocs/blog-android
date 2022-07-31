package com.pocs.presentation.view.post.detail

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ActivityPostDetailBinding
import com.pocs.presentation.model.post.PostDetailUiState
import com.pocs.presentation.view.post.edit.PostEditActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostDetailActivity : AppCompatActivity() {

    private var _binding: ActivityPostDetailBinding? = null
    private val binding: ActivityPostDetailBinding get() = requireNotNull(_binding)

    private val viewModel: PostDetailViewModel by viewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    private val id = intent.getIntExtra("id", -1)

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

        fetchPost()
        initToolBar()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                fetchPost()
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
                deletePost()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun fetchPost() {
        viewModel.fetchPost(id)
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun updateUi(uiState: PostDetailUiState) = with(binding) {
        invalidateOptionsMenu()
        progressBar.isVisible = uiState is PostDetailUiState.Loading
        when (uiState) {
            is PostDetailUiState.Success -> {
                val postDetail = uiState.postDetail

                title.text = postDetail.title
                subtitle.text =
                    getString(R.string.article_subtitle, postDetail.date, postDetail.writer)
                content.text = postDetail.content
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

    private fun deletePost() {
        viewModel.deletePost(id)
        this.finish()
    }
}