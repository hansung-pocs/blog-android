package com.pocs.presentation.view.home.post

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.google.android.material.snackbar.Snackbar
import com.pocs.domain.model.post.PostCategory
import com.pocs.domain.model.post.PostFilterType
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingFragment
import com.pocs.presentation.databinding.FragmentPostBinding
import com.pocs.presentation.extension.*
import com.pocs.presentation.model.post.PostUiState
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.component.HorizontalChips
import com.pocs.presentation.view.home.HomeViewModel
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.detail.PostDetailActivity
import kotlinx.coroutines.launch

class PostFragment : ViewBindingFragment<FragmentPostBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPostBinding
        get() = FragmentPostBinding::inflate

    private val postViewModel: PostViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PostAdapter(::onClickPost)

        initRecyclerView(adapter)
        initChips()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                postViewModel.uiState.collect {
                    updateUi(it, adapter)
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

    private fun initRecyclerView(adapter: PostAdapter) = with(binding) {
        recyclerView.adapter = adapter.withLoadStateFooter(
            PagingLoadStateAdapter { adapter.retry() }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addDividerDecoration()

        loadState.setListeners(adapter, refresh)
        adapter.registerObserverForScrollToTop(recyclerView, whenItemRangeMoved = true)

        fab.text = getString(R.string.write_post)
        fab.setOnClickListener { startPostCreateActivity() }
    }

    private fun initChips() {
        val items = PostFilterType.values().toMutableList().apply {
            // 스터디와 질문답변은 따로 탭이 있으니 칩에 보이지 않는다.
            removeAll(listOf(PostFilterType.STUDY, PostFilterType.QNA))
        }
        binding.chips.setContent {
            Mdc3Theme {
                val uiState = postViewModel.uiState.collectAsState()

                HorizontalChips(
                    items = items,
                    itemLabelBuilder = { stringResource(id = it.koreanStringResource) },
                    selectedItem = uiState.value.selectedPostFilterType,
                    onItemClick = postViewModel::updatePostFilterType
                )
            }
        }
    }

    private fun updateUi(uiState: PostUiState, adapter: PostAdapter) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.pagingData)
        binding.fab.isVisible = !uiState.isUserAnonymous
    }

    private fun onClickPost(postItemUiState: PostItemUiState) {
        val intent = PostDetailActivity.getIntent(
            requireContext(),
            id = postItemUiState.id
        )
        launcher?.launch(intent)
    }

    private fun showSnackBar(message: String) {
        // floating 버튼이 보일때는 버튼위에 띄우 안보일때는 bottom nav bar 위에 띄운다.
        if (binding.fab.isVisible) {
            Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
                anchorView = binding.fab
            }.show()
        } else {
            homeViewModel.showUserMessage(message)
        }
    }

    private fun startPostCreateActivity() {
        val intent = PostCreateActivity.getIntent(requireContext(), PostCategory.MEMORY)
        launcher?.launch(intent)
    }
}
