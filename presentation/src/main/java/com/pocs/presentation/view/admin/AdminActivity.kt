package com.pocs.presentation.view.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import com.google.android.material.tabs.TabLayoutMediator
import com.pocs.presentation.databinding.ActivityAdminBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class AdminActivity : AppCompatActivity() {

    private var _binding: ActivityAdminBinding? = null
    private val binding: ActivityAdminBinding get() = requireNotNull(_binding)

    private val viewModel: AdminViewModel by viewModels()

    private val tabTitleArray = arrayOf(
        "공지사항",
        "회원목록"
    )


    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AdminActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initToolBar()

        val viewPager = binding.viewpager
        val tabLayout = binding.tabLayout

        viewPager.adapter = AdminAdapter(supportFragmentManager,lifecycle)

        TabLayoutMediator(tabLayout,viewPager){ tab , position ->
            tab.text = tabTitleArray[position]
        }.attach()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun initToolBar() {
        setSupportActionBar(binding.toolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = "관리자 페이지"
    }

}