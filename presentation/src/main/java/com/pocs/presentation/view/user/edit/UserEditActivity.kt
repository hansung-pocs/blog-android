package com.pocs.presentation.view.user.edit

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
import com.pocs.presentation.model.user.item.UserDetailItemUiState
import dagger.hilt.android.AndroidEntryPoint

/**
 * 본인 정보를 수정할 수 있는 활동이다.
 */
@AndroidEntryPoint
class UserEditActivity : AppCompatActivity() {

    private val viewModel: UserEditViewModel by viewModels()

    companion object {
        fun getIntent(context: Context, userDetail: UserDetailItemUiState): Intent {
            return Intent(context, UserEditActivity::class.java).apply {
                putExtra("userDetail", userDetail)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, true)

        val userDetail = intent.getSerializableExtra("userDetail") as UserDetailItemUiState
        viewModel.init(userDetail)

        setContent {
            Mdc3Theme {
                UserEditScreen(
                    viewModel = viewModel,
                    navigateUp = ::finish,
                    onSuccessToSave = {
                        setResultRefresh(R.string.my_info_edited)
                    }
                )
            }
        }
    }
}