package com.pocs.presentation.view.admin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import com.google.android.material.tabs.TabLayoutMediator
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingActivity
import com.pocs.presentation.databinding.ActivityAdminBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AdminActivity : ViewBindingActivity<ActivityAdminBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityAdminBinding
        get() = ActivityAdminBinding::inflate

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, AdminActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolBar()

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        val tabTitleArray = resources.getStringArray(R.array.admin_tab)

        viewPager.adapter = AdminAdapter(supportFragmentManager, lifecycle)

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
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
        supportActionBar?.title = getString(R.string.admin_page)
    }
}
