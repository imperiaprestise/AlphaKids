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

        viewModel.getSession().observe(this) {
            intent = if (it.isLogin){
                Intent(this, MainActivity::class.java)
            } else {
                Intent(this, WelcomeActivity::class.java)
            }
        }

        Handler().postDelayed({
            startActivity(intent)
            finish()
        }, 3000)
    }


}