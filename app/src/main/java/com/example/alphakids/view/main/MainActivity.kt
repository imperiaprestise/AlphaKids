package com.example.alphakids.view.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.alphakids.R
import com.example.alphakids.view.WelcomeActivity
import com.example.alphakids.databinding.ActivityMainBinding
import com.example.alphakids.view.ViewModelFactory
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.navigation_home,
            R.id.navigation_write,
            R.id.navigation_book,
            R.id.navigation_profile
        ).build()

        val actionBar = supportActionBar
        if (actionBar != null) {
            setupActionBarWithNavController(navController, appBarConfiguration)
        }

        navView.setupWithNavController(navController)

        viewModel.getSession().observe(this){user ->
            Log.d("MainActivity", "user is logged in: ${user.isLogin} ")
            if (!user.isLogin){
                Log.d("MainActivity", "user info: ${user.username}")
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }

        setupView()
    }

    private fun setupView(){
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

}
