package com.pocs.presentation.view.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.snackbar.Snackbar
import com.pocs.presentation.R
import com.pocs.presentation.base.ViewBindingActivity
import com.pocs.presentation.databinding.ActivityHomeBinding
import com.pocs.presentation.model.post.HomeUiState
import com.pocs.presentation.view.admin.AdminActivity
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

        setSupportActionBar(binding.toolbar)

        initNavigationView()
        initBottomNavigationView()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect(::updateUi)
            }
        }
    }

    private fun initNavigationView() {
        with(binding) {
            val uiState = viewModel.uiState.value

            navigationView.menu.findItem(R.id.action_admin).apply {
                isVisible = uiState.isCurrentUserAdmin
            }
            if (uiState.isCurrentUserAnonymous) {
                navigationView.menu.findItem(R.id.action_user_list).apply {
                    title = getString(R.string.user_list_only_member)
                    isEnabled = false
                }
            }

            navigationView.setNavigationItemSelectedListener(::onSelectNavigationItem)
        }
    }

    private fun initBottomNavigationView() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        appBarConfiguration = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.PostFragment,
                R.id.StudyFragment,
                R.id.QuestionFragment
            ),
            drawerLayout = binding.drawerLayout,
            fallbackOnNavigateUpListener = ::onSupportNavigateUp
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.bottomNav.setupWithNavController(navController)
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
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun updateUi(homeUiState: HomeUiState) {
        if (homeUiState.userMessage != null) {
            showSnackBar(homeUiState.userMessage)
            viewModel.userMessageShown()
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).apply {
            anchorView = binding.bottomNav
        }.show()
    }
}
