package com.pocs.presentation.view.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingActivity
import com.pocs.presentation.databinding.ActivityHomeBinding
import com.pocs.presentation.view.admin.AdminActivity
import com.pocs.presentation.view.user.UserActivity
import com.pocs.presentation.view.user.detail.UserDetailActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : ViewBindingActivity<ActivityHomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding.toolbar)

        initNavigationView()
        initBottomNavigationView()
    }

    private fun initNavigationView() {
        with(binding) {
            val adminMenu = navigationView.menu.findItem(R.id.action_admin)
            adminMenu.isVisible = viewModel.isCurrentUserAdmin
            navigationView.setNavigationItemSelectedListener(::onSelectNavigationItem)
        }
    }

    private fun initBottomNavigationView() {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(R.id.NoticeFragment, R.id.ArticleFragment),
            drawerLayout = binding.drawerLayout,
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
    }

    private fun onSelectNavigationItem(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_user_list -> {
                // TODO: 회원이 아닌 경우 가입 or 로그인 엑티비티로 전환하기
                val intent = UserActivity.getIntent(this)
                startActivity(intent)
            }
            R.id.action_admin -> {
                val intent = AdminActivity.getIntent(this)
                startActivity(intent)
            }
        }
        binding.drawerLayout.close()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_my_info -> {
                val intent = UserDetailActivity.getIntent(this, viewModel.currentUserId)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }
}