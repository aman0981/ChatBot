package com.example.chatbot

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.chatbot.login.FireBaseUtil
import com.example.chatbot.login.LoginPhoneNumber

class splashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_FULLSCREEN,
//            WindowManager.LayoutParams.FLAG_FULLSCREEN
//        )
        Handler(Looper.getMainLooper()).postDelayed({

            if(FireBaseUtil.isLoggedIn()){
                startActivity( Intent(this@splashActivity, MainActivity::class.java))
            }
            else {
                val home = Intent(this@splashActivity, LoginPhoneNumber::class.java)
                startActivity(home)
                finish()}
                                                    },2000)
 }
}