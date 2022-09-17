package com.pocs.presentation.view.admin.user

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
import com.pocs.presentation.base.ViewBindingFragment
import com.pocs.presentation.databinding.FragmentAdminUserBinding
import com.pocs.presentation.extension.RefreshStateContract
import com.pocs.presentation.extension.registerObserverForScrollToTop
import com.pocs.presentation.extension.setListeners
import com.pocs.presentation.model.admin.AdminUserUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.admin.user.create.AdminUserCreateActivity
import com.pocs.presentation.view.user.UserAdapter
import com.pocs.presentation.view.user.detail.UserDetailActivity
import kotlinx.coroutines.launch

class AdminUserFragment : ViewBindingFragment<FragmentAdminUserBinding>() {

    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentAdminUserBinding
        get() = FragmentAdminUserBinding::inflate

    private var _adapter: UserAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: AdminUserViewModel by activityViewModels()

    private var launcher: ActivityResultLauncher<Intent>? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _adapter = UserAdapter(onClickCard = ::onClickCard)

        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(view.context)

            loadState.setListeners(adapter, refresh)
            adapter.registerObserverForScrollToTop(recyclerView)

            fab.setOnClickListener { startAdminUserCreateActivity() }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect(::updateUi)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
    }

    private fun onClickCard(userId: Int) {
        val intent = UserDetailActivity.getIntent(requireContext(), userId)
        launcher?.launch(intent)
    }

    private fun updateUi(uiState: AdminUserUiState) {
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.userPagingData)
    }

    private fun startAdminUserCreateActivity() {
        val intent = AdminUserCreateActivity.getIntent(requireContext())
        launcher?.launch(intent)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
            anchorView = binding.fab
        }.show()
    }
}
