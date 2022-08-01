package com.pocs.presentation.view.admin

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.pocs.presentation.view.admin.user.AdminUserFragment
import com.pocs.presentation.view.home.notice.NoticeFragment

class AdminAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    companion object {
        private const val NUM_TABS = 2
    }

    override fun getItemCount(): Int {
        return NUM_TABS
    }

    override fun createFragment(position: Int): Fragment {
        when (position) {
            0 -> return NoticeFragment()
            1 -> return AdminUserFragment()
        }
        throw IllegalArgumentException()
    }
}
