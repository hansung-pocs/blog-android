package com.pocs.presentation.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingActivity
import com.pocs.presentation.databinding.ActivityHomeBinding
import com.pocs.presentation.model.post.HomeUiState
import com.pocs.presentation.view.admin.AdminActivity
import com.pocs.presentation.view.login.LoginActivity
import com.pocs.presentation.view.setting.SettingActivity
import com.pocs.presentation.view.user.UserActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeActivity : ViewBindingActivity<ActivityHomeBinding>() {

    override val bindingInflater: (LayoutInflater) -> ActivityHomeBinding
        get() = ActivityHomeBinding::inflate

    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: HomeViewModel by viewModels()

    companion object {
        fun getIntent(context: Context): Intent {
            return Intent(context, HomeActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)

        showSplashUntilAppReady()

        setSupportActionBar(binding.toolbar)

        initNavigationView()
        initBottomNavigationView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }
    }

    private fun showSplashUntilAppReady() {
        val content: View = findViewById(android.R.id.content)
        content.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    val hideSplashScreen = viewModel.uiState.value.hideSplashScreen

                    return if (hideSplashScreen) {
                        content.viewTreeObserver.removeOnPreDrawListener(this)
                        true
                    } else {
                        false
                    }
                }
            }
        )
    }

    private fun initNavigationView() {
        with(binding) {
            val adminMenu = navigationView.menu.findItem(R.id.action_admin)
            adminMenu.isVisible = viewModel.uiState.value.isCurrentUserAdmin
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

    private fun updateUi(uiState: HomeUiState) {
        if (uiState.replaceToLoginActivity) {
            val intent = LoginActivity.getIntent(this)
            startActivity(intent)
            finish()
        }
    }

    private fun onSelectNavigationItem(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_user_list -> {
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
            R.id.action_setting -> {
                val intent = SettingActivity.getIntent(this)
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