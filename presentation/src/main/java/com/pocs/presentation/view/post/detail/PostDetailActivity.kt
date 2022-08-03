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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ActivityPostDetailBinding
import com.pocs.presentation.extension.getSnackBarMessage
import com.pocs.presentation.extension.putSnackBarMessage
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

    companion object {
        fun getIntent(context: Context, id: Int, isDeleted: Boolean): Intent {
            return Intent(context, PostDetailActivity::class.java).apply {
                putExtra("id", id)
                putExtra("isDeleted", isDeleted)
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

            it.getSnackBarMessage()?.let { message ->
                showSnackBar(message)
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

        menu.children.forEach {
            when (it.itemId) {
                R.id.action_edit_post -> {
                    it.isVisible = (uiState as? PostDetailUiState.Success)?.canEditPost ?: false
                }
                R.id.action_delete_post -> {
                    it.isVisible = (uiState as? PostDetailUiState.Success)?.canDeletePost ?: false
                }
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_edit_post -> {
                startPostEditActivity()
                true
            }
            R.id.action_delete_post -> {
                showRecheckDialog()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onDeleteSuccess() {
        val intent = Intent().putSnackBarMessage(getString(R.string.post_deleted))
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun fetchPost() {
        val id = intent.getIntExtra("id", -1)
        val isDeleted = intent.getBooleanExtra("isDeleted", false)

        viewModel.fetchPost(id, isDeleted)
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
                subtitle.text = getString(
                    R.string.post_subtitle,
                    postDetail.date,
                    postDetail.writer.name
                )
                content.text = postDetail.content
                deleted.isVisible = uiState.isDeleted

                if (uiState.isSuccessToDelete) {
                    onDeleteSuccess()
                }
                if (uiState.errorMessage != null) {
                    showSnackBar(uiState.errorMessage)
                    viewModel.shownErrorMessage()
                }
            }
            is PostDetailUiState.Failure -> {
                title.text = getString(R.string.failed_to_load)
                content.text = uiState.message
            }
            else -> {}
        }
    }

    private fun showRecheckDialog() {
        MaterialAlertDialogBuilder(this).apply {
            setTitle(getString(R.string.are_you_sure_you_want_to_delete))
            setPositiveButton(getString(R.string.delete)) { _, _ ->
                requestPostDeleting()
            }
            setNegativeButton(getString(R.string.cancel)) { _, _ -> }
        }.show()
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun requestPostDeleting() {
        val postId = intent.getIntExtra("id", -1)
        viewModel.requestPostDeleting(postId)
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
}