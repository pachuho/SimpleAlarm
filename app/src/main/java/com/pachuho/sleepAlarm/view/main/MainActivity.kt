package com.pachuho.sleepAlarm.view.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

import com.pachuho.sleepAlarm.base.BaseActivity
import sleepAlarm.R
import sleepAlarm.databinding.ActivityMainBinding
import timber.log.Timber

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    private val broadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(p0: Context?, p1: Intent?) {
            Timber.e("goOff!")
            findNavController(R.id.nav_host_container).navigate(R.id.action_alarmFragment_to_goOffFragment)
            return
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, IntentFilter("goOff"))

        binding.apply {
            vm = viewModel
        }
//        setUpBottomNavigationBar()
    }

//    private fun setUpBottomNavigationBar() {
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_container) as NavHostFragment
//        navController = navHostFragment.navController
//        binding.bottomNavigation.setupWithNavController(navController)
//    }

    override fun onBackPressed() {
        super.onBackPressed()
        Timber.i("onBackPressed!")
    }
}