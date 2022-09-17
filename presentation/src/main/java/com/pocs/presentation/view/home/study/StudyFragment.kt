package com.pocs.presentation.view.home.study

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.core.view.isVisible
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
import com.pocs.presentation.extension.*
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.model.study.StudyUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.home.HomeViewModel
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.detail.PostDetailActivity
import kotlinx.coroutines.launch

// TODO: Study 기획이 구체화 되면 업데이트하기. 현재는 임시임
class StudyFragment : ViewBindingFragment<FragmentPostBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentPostBinding
        get() = FragmentPostBinding::inflate

    private val studyViewModel: StudyViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PostAdapter(::onClickPost)

        initRecyclerView(adapter)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                studyViewModel.uiState.collect {
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
        adapter.registerObserverForScrollToTop(recyclerView)

        fab.text = getString(R.string.write_post)
        fab.setOnClickListener { startPostCreateActivity() }
    }

    private fun updateUi(uiState: StudyUiState, adapter: PostAdapter) {
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
        val intent = PostCreateActivity.getIntent(requireContext(), PostCategory.STUDY)
        launcher?.launch(intent)
    }
}
