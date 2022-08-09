package com.pocs.presentation.view.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.pocs.presentation.base.ViewBindingActivity
import com.pocs.presentation.databinding.ActivityUserBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UserActivity : ViewBindingActivity<ActivityUserBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityUserBinding
        get() = ActivityUserBinding::inflate

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, UserActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolBar()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}