package com.pocs.presentation.view.user.anonymous

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.accompanist.themeadapter.material3.Mdc3Theme
import com.pocs.presentation.R
import com.pocs.presentation.extension.setResultRefresh
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AnonymousCreateActivity : AppCompatActivity() {

    private val viewModel: AnonymousCreateViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AnonymousCreateActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        setContent {
            Mdc3Theme(this) {
                AnonymousCreateScreen(
                    uiState = viewModel.uiState,
                    navigateUp = ::finish,
                    onSuccessToCreate = {
                        setResultRefresh(R.string.sign_up_as_anonymous)
                    }
                )
            }
        }
    }
}
