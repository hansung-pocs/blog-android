package com.pocs.presentation.view.admin.user.create

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.presentation.R
import com.pocs.presentation.extension.setResultRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminUserCreateActivity : AppCompatActivity() {

    private val viewModel: AdminUserCreateViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AdminUserCreateActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            Mdc3Theme(this) {
                AdminUserCreateScreen(
                    viewModel.uiState,
                    navigateUp = ::finish,
                    onSuccessToCreate = {
                        setResultRefresh(R.string.user_created)
                    }
                )
            }
        }
    }
}