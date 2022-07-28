package com.pocs.presentation.view.home.notice

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocs.domain.model.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.databinding.FragmentNoticeBinding
import com.pocs.presentation.model.NoticeUiState
import com.pocs.presentation.model.PostItemUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.detail.PostDetailActivity
import kotlinx.coroutines.launch

class NoticeFragment : Fragment(R.layout.fragment_notice) {

    private var _binding: FragmentNoticeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoticeViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoticeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = PostAdapter(::onClickArticle)
        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(view.context)

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

            fab.setOnClickListener { startPostCreateActivity() }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect {
                        updateUi(it, adapter)
                    }
                }
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                adapter.refresh()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(uiState: NoticeUiState, adapter: PostAdapter) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.noticePagingData)
    }

    private fun onClickArticle(postItemUiState: PostItemUiState) {
        val intent = PostDetailActivity.getIntent(requireContext(), postItemUiState.id)
        startActivity(intent)
    }

    private fun startPostCreateActivity() {
        // TODO: 글 작성 후 성공했다면 adapter refresh 하기
        val intent = PostCreateActivity.getIntent(requireContext(), PostCategory.NOTICE)
        launcher?.launch(intent)
    }
}