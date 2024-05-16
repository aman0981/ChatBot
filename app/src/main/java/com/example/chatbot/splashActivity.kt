package com.example.chatbot

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot.login.LoginPhoneNumber

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

                val home = Intent(this@splashActivity, LoginPhoneNumber::class.java)
                startActivity(home)
                finish()
                                                    },4000)
 }
}