package com.pocs.presentation.view.post.edit

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.google.accompanist.appcompattheme.AppCompatTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PostEditActivity: AppCompatActivity() {

    companion object {
        fun getIntent(context: Context, id: Int): Intent {
            return Intent(context, PostEditActivity::class.java).apply {
                putExtra("id", id)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppCompatTheme {
                PostEditPage()
            }
        }
    }
}