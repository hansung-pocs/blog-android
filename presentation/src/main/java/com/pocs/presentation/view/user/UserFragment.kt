package com.pocs.presentation.view.user

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.presentation.R
import com.pocs.presentation.databinding.FragmentUserBinding
import com.pocs.presentation.model.user.UserUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import kotlinx.coroutines.launch


class UserFragment : Fragment(R.layout.fragment_user) {

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

        val adapter = UserAdapter()
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

            sortBox.setOnClickListener { showSortingMethodPopUpMenu() }

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

    private fun showSortingMethodPopUpMenu() {
        PopupMenu(requireContext(), binding.sortButton).apply {
            menuInflater.inflate(R.menu.menu_sorting_method_pop_up, menu)
            setOnMenuItemClickListener {
                val newSortingMethod = when (it.itemId) {
                    R.id.action_generation_descending -> UserListSortingMethod.GENERATION
                    R.id.action_student_id_ascending -> UserListSortingMethod.STUDENT_ID
                    else -> throw IllegalArgumentException()
                }
                viewModel.updateSortingMethod(newSortingMethod)
                true
            }
        }.show()
    }

    private fun updateUi(uiState: UserUiState, adapter: UserAdapter) {
        adapter.submitData(lifecycle, uiState.userPagingData)
        val stringResource = when (uiState.sortingMethod) {
            UserListSortingMethod.STUDENT_ID -> R.string.sorting_by_student_id_ascending
            UserListSortingMethod.GENERATION -> R.string.sorting_by_generation_descending
        }
        binding.sortText.text = getString(stringResource)
    }
}