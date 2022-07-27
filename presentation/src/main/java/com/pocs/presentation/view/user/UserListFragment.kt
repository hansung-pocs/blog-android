package com.pocs.presentation.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocs.presentation.R
import com.pocs.presentation.databinding.FragmentUserBinding
import com.pocs.presentation.model.UserUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.user.detail.UserDetailActivity
import kotlinx.coroutines.launch

class UserListFragment : Fragment(R.layout.fragment_user) {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private val viewModel: UserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = UserAdapter(::startUserDetailActivity)
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

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect {
                        updateUi(it, adapter)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun updateUi(uiState: UserUiState, adapter: UserAdapter) {
        adapter.submitData(lifecycle, uiState.userPagingData)
    }

    private fun startUserDetailActivity(userId: Int) {
        val intent = UserDetailActivity.getIntent(requireContext(), userId)
        startActivity(intent)
    }
}