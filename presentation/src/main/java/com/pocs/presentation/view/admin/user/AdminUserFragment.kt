package com.pocs.presentation.view.admin.user

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
import com.pocs.domain.model.user.UserType
import com.pocs.presentation.R
import com.pocs.presentation.databinding.FragmentAdminUserBinding
import com.pocs.presentation.model.admin.AdminUserUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.user.UserAdapter
import kotlinx.coroutines.launch

class AdminUserFragment : Fragment(R.layout.fragment_admin_user) {

    private var _binding: FragmentAdminUserBinding? = null
    private val binding get() = _binding!!

    private var _adapter: UserAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: AdminUserViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAdminUserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: 로그인 기능 구현되면 현재 로그인한 유저의 타입을 넘기기
        _adapter = UserAdapter(UserType.ADMIN)

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
                    viewModel.uiState.collect(::updateUi)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _adapter = null
    }

    private fun updateUi(uiState: AdminUserUiState) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.userPagingData)
    }
}