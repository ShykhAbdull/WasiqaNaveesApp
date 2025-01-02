package com.hashimnaqvillc.wasiqanaveesapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        // Delay for 2 seconds
        Handler().postDelayed({
            // Navigate to MainActivity
            val intent = Intent(this, Page1Activity::class.java)
            startActivity(intent)
            finish() // Close splash screen
        }, 2200) // 2000 milliseconds = 2 seconds
    }
}