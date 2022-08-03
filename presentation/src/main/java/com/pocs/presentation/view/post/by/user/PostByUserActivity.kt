package com.pocs.presentation.view.post.by.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ActivityPostByUserBinding
import com.pocs.presentation.extension.getSnackBarMessage
import com.pocs.presentation.extension.setListeners
import com.pocs.presentation.model.post.PostByUserUiState
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.detail.PostDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostByUserActivity : AppCompatActivity() {

    private var _binding: ActivityPostByUserBinding? = null
    private val binding: ActivityPostByUserBinding get() = requireNotNull(_binding)

    private var launcher: ActivityResultLauncher<Intent>? = null

    private val viewModel: PostByUserViewModel by viewModels()

    companion object {
        fun getIntent(context: Context, userId: Int, userName: String): Intent {
            return Intent(context, PostByUserActivity::class.java).apply {
                putExtra("userId", userId)
                putExtra("userName", userName)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityPostByUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()
        fetchPosts()

        val adapter = PostAdapter(onClickItem = ::startPostDetailActivity)
        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(this@PostByUserActivity)

            loadState.setListeners(adapter)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { updateUi(it, adapter) }
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                adapter.refresh()

                it.getSnackBarMessage()?.let { message ->
                    showSnackBar(message)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        launcher = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun fetchPosts() {
        val userId = intent.getIntExtra("userId", -1)
        assert(userId != -1)
        viewModel.fetchPosts(userId)
    }

    private fun initToolBar() {
        val name = intent.getStringExtra("userName") ?: getString(R.string.unknown)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.post_by_user_title, name)
    }

    private fun updateUi(uiState: PostByUserUiState, adapter: PostAdapter) {
        adapter.submitData(lifecycle, uiState.postPagingData)
    }

    private fun startPostDetailActivity(postItemUiState: PostItemUiState) {
        val intent = PostDetailActivity.getIntent(
            this,
            id = postItemUiState.id,
            isDeleted = postItemUiState.isDeleted
        )
        launcher?.launch(intent)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}