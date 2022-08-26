package com.pocs.presentation.view.user

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingFragment
import com.pocs.presentation.databinding.FragmentUserBinding
import com.pocs.presentation.extension.RefreshStateContract
import com.pocs.presentation.extension.registerObserverForScrollToTop
import com.pocs.presentation.extension.setListeners
import com.pocs.presentation.model.user.UserUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.user.detail.UserDetailActivity
import kotlinx.coroutines.launch

class UserFragment : ViewBindingFragment<FragmentUserBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentUserBinding
        get() = FragmentUserBinding::inflate

    private var _adapter: UserAdapter? = null
    private val adapter get() = _adapter!!

    private var launcher: ActivityResultLauncher<Intent>? = null

    private val viewModel: UserViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = UserAdapter(onClickCard = ::onClickCard)

        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(view.context)

            loadState.setListeners(adapter, refresh)
            adapter.registerObserverForScrollToTop(
                recyclerView,
                whenItemRangeMoved = true,
                whenItemInsertedFirst = false
            )

            sortBox.setOnClickListener { showSortingMethodPopUpMenu() }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
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
        _adapter = null
    }

    private fun onClickCard(userId: Int) {
        val intent = UserDetailActivity.getIntent(requireContext(), userId)
        launcher?.launch(intent)
    }

    private fun showSortingMethodPopUpMenu() {
        PopupMenu(requireContext(), binding.sortButton).apply {
            menuInflater.inflate(R.menu.menu_sorting_method_pop_up, menu)
            setOnMenuItemClickListener {
                val newSortingMethod = when (it.itemId) {
                    R.id.action_created_at_descending -> UserListSortingMethod.CREATED_AT
                    R.id.action_generation_descending -> UserListSortingMethod.GENERATION
                    R.id.action_student_id_ascending -> UserListSortingMethod.STUDENT_ID
                    else -> throw IllegalArgumentException()
                }
                viewModel.updateSortingMethod(newSortingMethod)
                true
            }
        }.show()
    }

    private fun updateUi(uiState: UserUiState) {
        val sortTextStringRes = when (uiState.sortingMethod) {
            UserListSortingMethod.CREATED_AT -> R.string.sorting_by_created_at_descending
            UserListSortingMethod.STUDENT_ID -> R.string.sorting_by_student_id_ascending
            UserListSortingMethod.GENERATION -> R.string.sorting_by_generation_descending
        }
        val pagingData = if (uiState.enabledSearchMode) {
            uiState.searchPagingData
        } else {
            uiState.userPagingData
        }

        binding.sortText.text = getString(sortTextStringRes)
        adapter.submitData(viewLifecycleOwner.lifecycle, pagingData)

        if (uiState.errorMessageRes != null) {
            showSnackBar(getString(uiState.errorMessageRes))
            viewModel.errorMessageShown()
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}