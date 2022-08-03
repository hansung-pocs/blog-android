package com.pocs.presentation.view.user

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.pocs.domain.model.user.UserListSortingMethod
import com.pocs.presentation.R
import com.pocs.presentation.databinding.FragmentUserBinding
import com.pocs.presentation.extension.getSnackBarMessage
import com.pocs.presentation.extension.setListeners
import com.pocs.presentation.model.user.UserUiState
import com.pocs.presentation.paging.PagingLoadStateAdapter
import com.pocs.presentation.view.user.detail.UserDetailActivity
import kotlinx.coroutines.launch

class UserFragment : Fragment(R.layout.fragment_user) {

    private var _binding: FragmentUserBinding? = null
    private val binding get() = _binding!!

    private var _adapter: UserAdapter? = null
    private val adapter get() = _adapter!!

    private var launcher: ActivityResultLauncher<Intent>? = null

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

        _adapter = UserAdapter(viewModel.uiState.value.currentUserType, onClickCard = ::onClickCard)

        binding.apply {
            recyclerView.adapter = adapter.withLoadStateFooter(
                PagingLoadStateAdapter { adapter.retry() }
            )
            recyclerView.layoutManager = LinearLayoutManager(view.context)

            loadState.setListeners(adapter)

            sortBox.setOnClickListener { showSortingMethodPopUpMenu() }

            viewLifecycleOwner.lifecycleScope.launch {
                viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.uiState.collect(::updateUi)
                }
            }
        }

        launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                adapter.refresh()

                it.getSnackBarMessage()?.let { message ->
                    showSnackBar(message)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
        adapter.submitData(viewLifecycleOwner.lifecycle, uiState.userPagingData)
        val stringResource = when (uiState.sortingMethod) {
            UserListSortingMethod.STUDENT_ID -> R.string.sorting_by_student_id_ascending
            UserListSortingMethod.GENERATION -> R.string.sorting_by_generation_descending
        }
        binding.sortText.text = getString(stringResource)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}