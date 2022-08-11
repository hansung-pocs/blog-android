package com.pocs.presentation.view.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.presentation.model.auth.LoginUiState
import com.pocs.presentation.view.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        if (viewModel.uiState.value.isLoggedIn) {
            navigateToHomeActivity()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }

        setContent {
            Mdc3Theme(this) {
                LoginScreen(
                    viewModel = viewModel,
                    onBrowsAsNonMemberClick = ::navigateToHomeActivity
                )
            }
        }
    }

    private fun updateUi(uiState: LoginUiState) {
        if (uiState.isLoggedIn) {
            navigateToHomeActivity()
        }
    }

    private fun navigateToHomeActivity() {
        val intent = HomeActivity.getIntent(this)
        startActivity(intent)
        finish()
    }
}