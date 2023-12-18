package com.example.alphakids

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    private val splashOut: Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            val splashIntent = Intent(this@SplashScreen, WelcomeActivity::class.java)
            startActivity(splashIntent)
            finish()
        }, splashOut)
    }
}