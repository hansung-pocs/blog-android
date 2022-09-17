package com.pocs.presentation.view.setting

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.presentation.view.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingActivity : AppCompatActivity() {

    private val viewModel: SettingViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, SettingActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Mdc3Theme(this) {
                SettingScreen(
                    viewModel = viewModel,
                    onSuccessToLogout = ::startLoginActivityAndClearPrevious
                )
            }
        }
    }

    private fun startLoginActivityAndClearPrevious() {
        val intent = LoginActivity.getIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}
