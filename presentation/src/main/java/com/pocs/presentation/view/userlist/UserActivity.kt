package com.pocs.presentation.view.userlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocs.presentation.databinding.ActivityUserListBinding
import com.pocs.presentation.model.UserUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserActivity : AppCompatActivity() {

    private var _binding: ActivityUserListBinding? = null
    private val binding: ActivityUserListBinding get() = requireNotNull(_binding)

    private val viewModel: UserViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUserListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initToolBar()
        initRecyclerView()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerView() = with(binding) {
        val adapter = UserAdapter()

        recyclerView.adapter = adapter.withLoadStateFooter(
            PagingLoadStateAdapter { adapter.retry() }
        )
        recyclerView.layoutManager = LinearLayoutManager(this@UserActivity)

        val loadStateBinding = loadState
        loadStateBinding.retryButton.setOnClickListener {
            adapter.retry()
        }

        adapter.addLoadStateListener { loadStates ->
            val isError = loadStates.refresh is LoadState.Error
            loadStateBinding.progressBar.isVisible = loadStates.refresh is LoadState.Loading
            loadStateBinding.retryButton.isVisible = isError
            loadStateBinding.errorMsg.isVisible = isError
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
                    updateUi(it, adapter)
                }
            }
        }
    }

    private fun updateUi(uiState: UserUiState, adapter: UserAdapter) {
        adapter.submitData(lifecycle, uiState.userPagingData)
    }
}