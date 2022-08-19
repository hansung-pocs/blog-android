package com.pocs.presentation.view.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.res.stringResource
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingActivity
import com.pocs.presentation.databinding.ActivityUserBinding
import com.pocs.presentation.view.component.appbar.SearchAppBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : ViewBindingActivity<ActivityUserBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityUserBinding
        get() = ActivityUserBinding::inflate

    private val viewModel: UserViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolBar()
    }

    private fun initToolBar() {
        binding.toolBar.setContent {
            Mdc3Theme(this) {
                val uiState = viewModel.uiState.collectAsState()

                SearchAppBar(
                    title = stringResource(id = R.string.user_list),
                    onSearch = viewModel::search,
                    enabledSearchMode = uiState.value.enabledSearchMode,
                    onSearchModeChange = viewModel::onSearchModeChange
                )
            }
        }
    }
}