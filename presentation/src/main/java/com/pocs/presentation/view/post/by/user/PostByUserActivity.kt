package com.pocs.presentation.view.post.by.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingActivity
import com.pocs.presentation.databinding.ActivityPostByUserBinding
import com.pocs.presentation.extension.RefreshStateContract
import com.pocs.presentation.extension.addDividerDecoration
import com.pocs.presentation.extension.setListeners
import com.pocs.presentation.model.post.PostByUserUiState
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.detail.PostDetailActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PostByUserActivity : ViewBindingActivity<ActivityPostByUserBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityPostByUserBinding
        get() = ActivityPostByUserBinding::inflate

    private var launcher: ActivityResultLauncher<Intent>? = null

    private val viewModel: PostByUserViewModel by viewModels()

    companion object {
        fun getIntent(context: Context, userId: Int, name: String): Intent {
            return Intent(context, PostByUserActivity::class.java).apply {
                putExtra("userId", userId)
                putExtra("name", name)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolBar()
        fetchPosts()

        val adapter = PostAdapter(onClickItem = ::startPostDetailActivity)
        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(this@PostByUserActivity)
            recyclerView.addDividerDecoration()

            loadState.setListeners(adapter, refresh)
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { updateUi(it, adapter) }
            }
        }

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                adapter.refresh()
                it.message?.let { message -> showSnackBar(message) }
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
        val name = intent.getStringExtra("name") ?: throw Exception("유저 이름이 전달되지 않음")
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
            id = postItemUiState.id
        )
        launcher?.launch(intent)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}