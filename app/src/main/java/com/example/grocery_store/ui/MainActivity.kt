package com.example.grocery_store.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.grocery_store.R
import com.example.grocery_store.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navHostFragment: NavHostFragment

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        binding.apply {
            setSupportActionBar(toolbar)
            navHostFragment =
                supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
            navController = navHostFragment.navController
            bottomNavigation.setupWithNavController(navController)
            bottomNavigation.setOnItemReselectedListener {}
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            NavigationUI.setupActionBarWithNavController(
                this@MainActivity, navController, appBarConfiguration
            )
            navController.addOnDestinationChangedListener { _, destination, _ ->
                when (destination.id) {
                    R.id.dashboardScreen, R.id.historyFragment -> bottomNavigation.visibility =
                        View.VISIBLE

                    else -> bottomNavigation.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}