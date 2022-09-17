package com.pocs.presentation.view.home.question

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
import com.pocs.presentation.databinding.FragmentQuestionBinding
import com.pocs.presentation.extension.RefreshStateContract
import com.pocs.presentation.extension.addDividerDecoration
import com.pocs.presentation.extension.registerObserverForScrollToTop
import com.pocs.presentation.extension.setListeners
import com.pocs.presentation.model.post.item.PostItemUiState
import com.pocs.presentation.model.question.QuestionUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.detail.PostDetailActivity
import kotlinx.coroutines.launch

class QuestionFragment : ViewBindingFragment<FragmentQuestionBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentQuestionBinding
        get() = FragmentQuestionBinding::inflate

    private val viewModel: QuestionViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PostAdapter(::onClickPost)

        initRecyclerView(adapter)

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
        recyclerView.adapter = adapter.withLoadStateFooter(
            PagingLoadStateAdapter { adapter.retry() }
        )
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.addDividerDecoration()

        loadState.setListeners(adapter, refresh)
        adapter.registerObserverForScrollToTop(recyclerView)

        fab.text = getString(R.string.write_question)
        fab.setOnClickListener { startQuestionCreateActivity() }
    }

    private fun onClickPost(postItemUiState: PostItemUiState) {
        val intent = PostDetailActivity.getIntent(
            requireContext(),
            id = postItemUiState.id
        )
        launcher?.launch(intent)
    }

    private fun updateUi(uiState: QuestionUiState, adapter: PostAdapter) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.pagingData)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
            anchorView = binding.fab
        }.show()
    }

    private fun startQuestionCreateActivity() {
        val intent = PostCreateActivity.getIntent(requireContext(), PostCategory.QNA)
        launcher?.launch(intent)
    }
}
