package com.pocs.presentation.view.user.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import com.google.android.material.composethemeadapter3.Mdc3Theme
import com.pocs.domain.model.User
import dagger.hilt.android.AndroidEntryPoint

/**
 * 본인 정보를 수정할 수 있는 활동이다.
 */
@AndroidEntryPoint
class UserEditActivity : AppCompatActivity() {

    private val viewModel: UserEditViewModel by viewModels()

    companion object {
        fun getIntent(context: Context, user: User): Intent {
            return Intent(context, UserEditActivity::class.java).apply {
                putExtra("user", user)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        val user = intent.getSerializableExtra("user") as User
        viewModel.init(user)

        setContent {
            Mdc3Theme {
                UserEditScreen(viewModel = viewModel, navigateUp = ::finish)
            }
        }
    }
}