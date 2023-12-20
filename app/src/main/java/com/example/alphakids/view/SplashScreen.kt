package com.example.alphakids.view

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.activity.viewModels
import com.example.alphakids.data.pref.UserPreference
import com.example.alphakids.data.pref.dataStore
import com.example.alphakids.databinding.ActivitySplashScreenBinding
import com.example.alphakids.view.main.MainActivity
import com.example.alphakids.view.main.MainViewModel
@SuppressLint("SplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding: ActivitySplashScreenBinding
    private val viewModel: MainViewModel by viewModels{
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        var intent: Intent? = null

        viewModel.getSession().observe(this) { user ->
            val destinationClass = if (user.isLogin) {
                MainActivity::class.java
            } else {
                WelcomeActivity::class.java
            }

            val intent = Intent(this, destinationClass)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private val REQUEST_CODE = 123 // You can use any unique request code

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            // Handle the result if needed
            finish()
        }
    }
}