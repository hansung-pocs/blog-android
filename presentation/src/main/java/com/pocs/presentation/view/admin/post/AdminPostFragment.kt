package com.pocs.presentation.view.admin.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingFragment
import com.pocs.presentation.databinding.FragmentPostBinding
import com.pocs.presentation.extension.RefreshStateContract
import com.pocs.presentation.extension.addDividerDecoration
import com.pocs.presentation.extension.registerObserverForScrollToTop
import com.pocs.presentation.extension.setListeners
import com.pocs.presentation.model.admin.AdminPostUiState
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.detail.PostDetailActivity
import kotlinx.coroutines.launch

class AdminPostFragment : ViewBindingFragment<FragmentPostBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPostBinding
        get() = FragmentPostBinding::inflate

    private val viewModel: AdminPostViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PostAdapter(::onClickPost)
        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(view.context)
            recyclerView.addDividerDecoration()

            loadState.setListeners(adapter, refresh)
            adapter.registerObserverForScrollToTop(recyclerView)

            fab.text = getString(R.string.write_notice)
            fab.setOnClickListener { startPostCreateActivity() }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect {
                        updateUi(it, adapter)
                    }
                }
            }
        }

        launcher = registerForActivityResult(RefreshStateContract()) {
            if (it != null) {
                adapter.refresh()
                it.message?.let { message -> showSnackBar(message) }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        launcher = null
    }

    private fun updateUi(uiState: AdminPostUiState, adapter: PostAdapter) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.postPagingData)
    }

    private fun onClickPost(postItemUiState: PostItemUiState) {
        val intent = PostDetailActivity.getIntent(
            requireContext(),
            id = postItemUiState.id,
            isDeleted = postItemUiState.isDeleted
        )
        launcher?.launch(intent)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
            anchorView = binding.fab
        }.show()
    }

    private fun startPostCreateActivity() {
        val intent = PostCreateActivity.getIntent(requireContext(), PostCategory.NOTICE)
        launcher?.launch(intent)
    }
}