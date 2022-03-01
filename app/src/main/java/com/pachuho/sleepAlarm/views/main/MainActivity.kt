package com.pachuho.sleepAlarm.views.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.pachuho.sleepAlarm.base.BaseActivity
import sleepAlarm.R
import sleepAlarm.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.apply {
            vm = viewModel
        }
        setUpBottomNavigationBar()
    }

    private fun setUpBottomNavigationBar() = with(binding) {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
        navController = navHostFragment.navController

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setupWithNavController(navController)
    }
}