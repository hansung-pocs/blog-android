package com.pocs.presentation.view.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.pocs.presentation.databinding.ActivityAdminBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private var _binding: ActivityAdminBinding? = null
    private val binding : ActivityAdminBinding get() = requireNotNull(_binding)

    private val viewModel: AdminViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AdminActivity::class.java)
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}