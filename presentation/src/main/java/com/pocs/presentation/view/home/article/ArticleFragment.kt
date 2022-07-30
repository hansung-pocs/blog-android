package com.pocs.presentation.view.home.article

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocs.domain.model.post.PostCategory
import com.pocs.presentation.R
import com.pocs.presentation.databinding.ContentLoadStateBinding
import com.pocs.presentation.databinding.FragmentArticleBinding
import com.pocs.presentation.model.ArticleUiState
import com.pocs.presentation.model.PostItemUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.post.adapter.PostAdapter
import com.pocs.presentation.view.post.create.PostCreateActivity
import com.pocs.presentation.view.post.detail.PostDetailActivity
import kotlinx.coroutines.launch
import java.net.ConnectException

class ArticleFragment : Fragment(R.layout.fragment_article) {

    private var _binding: FragmentArticleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ArticleViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentArticleBinding.inflate(inflater, container, false)
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
                listenLoadState(loadStates, loadStateBinding)
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
            if (it.resultCode == RESULT_OK) {
                adapter.refresh()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun listenLoadState(
        loadStates: CombinedLoadStates,
        loadStateBinding: ContentLoadStateBinding
    ) {
        val refreshLoadState = loadStates.refresh
        val isError = refreshLoadState is LoadState.Error
        loadStateBinding.apply {
            progressBar.isVisible = refreshLoadState is LoadState.Loading
            retryButton.isVisible = isError
            errorMsg.isVisible = isError
            if (refreshLoadState is LoadState.Error) {
                errorMsg.text = when (val exception = refreshLoadState.error) {
                    is ConnectException -> getString(R.string.fail_to_connect)
                    else -> exception.message
                }
            }
        }
    }

    private fun updateUi(uiState: ArticleUiState, adapter: PostAdapter) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.articlePagingData)
    }

    private fun onClickArticle(postItemUiState: PostItemUiState) {
        val intent = PostDetailActivity.getIntent(requireContext(), postItemUiState.id)
        startActivity(intent)
    }

    private fun startPostCreateActivity() {
        // TODO: 적절한 카테고리로 수정하기
        val intent = PostCreateActivity.getIntent(requireContext(), PostCategory.STUDY)
        launcher?.launch(intent)
    }
}