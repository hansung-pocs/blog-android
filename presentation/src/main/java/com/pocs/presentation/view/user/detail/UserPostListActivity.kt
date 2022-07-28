package com.pocs.presentation.view.user.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pocs.presentation.databinding.ActivityUserPostListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserPostListActivity : AppCompatActivity() {

    private var _binding: ActivityUserPostListBinding? = null
    private val binding: ActivityUserPostListBinding get() = requireNotNull(_binding)

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, UserPostListActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUserPostListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // TODO 유저이름과 간단한 문구넣기
        supportActionBar?.title = "TODO"
    }
}