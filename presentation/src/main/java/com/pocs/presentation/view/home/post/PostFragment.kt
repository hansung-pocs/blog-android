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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
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
import com.pocs.presentation.view.home.HomeActivity
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.detail.PostDetailActivity
import kotlinx.coroutines.launch

class PostFragment : ViewBindingFragment<FragmentPostBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPostBinding
        get() = FragmentPostBinding::inflate

    private val viewModel: PostViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val extendedFab = requireActivity().findViewById<ExtendedFloatingActionButton>(R.id.fab)
        val adapter = PostAdapter(::onClickPost)

        initRecyclerView(adapter)
        initExtendedFloatingActionButton(extendedFab)
        initChips()

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect {
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
        val activity = requireActivity() as HomeActivity
        recyclerView.adapter = adapter.withLoadStateFooter(
            PagingLoadStateAdapter { adapter.retry() }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addDividerDecoration()
        recyclerView.setOnScrollChangeListener(activity::onScrollChangeListener)

        loadState.setListeners(adapter, refresh)
        adapter.registerObserverForScrollToTop(recyclerView, whenItemRangeMoved = true)
    }

    private fun initChips() {
        val items = PostFilterType.values().toMutableList().apply {
            // 스터디와 질문답변은 따로 탭이 있으니 칩에 보이지 않는다.
            removeAll(listOf(PostFilterType.STUDY, PostFilterType.QNA))
        }
        binding.chips.setContent {
            Mdc3Theme {
                val uiState = viewModel.uiState.collectAsState()

                HorizontalChips(
                    items = items,
                    itemLabelBuilder = { stringResource(id = it.koreanStringResource) },
                    selectedItem = uiState.value.selectedPostFilterType,
                    onItemClick = viewModel::updatePostFilterType
                )
            }
        }
    }

    private fun initExtendedFloatingActionButton(
        extendedFloatingActionButton: ExtendedFloatingActionButton
    ) {
        extendedFloatingActionButton.apply {
            isVisible = !viewModel.uiState.value.isUserAnonymous
            text = getString(R.string.write_post)
            setOnClickListener { startPostCreateActivity() }
        }
    }

    private fun updateUi(uiState: PostUiState, adapter: PostAdapter) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.pagingData)
    }

    private fun onClickPost(postItemUiState: PostItemUiState) {
        val intent = PostDetailActivity.getIntent(
            requireContext(),
            id = postItemUiState.id
        )
        launcher?.launch(intent)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun startPostCreateActivity() {
        val intent = PostCreateActivity.getIntent(requireContext(), PostCategory.MEMORY)
        launcher?.launch(intent)
    }
}
