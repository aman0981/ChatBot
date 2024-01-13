package com.example.chatbot.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.chatbot.R
import com.hbb20.CountryCodePicker

class LoginPhoneNumber : AppCompatActivity() {

    lateinit var countryCodePicker : CountryCodePicker
    lateinit var phoneInput : EditText
    lateinit var sendOtpButton: Button
    lateinit var progressBar : ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_phone_number)

        countryCodePicker = findViewById(R.id.login_countrycode)
        phoneInput = findViewById(R.id.login_mobile_number)
        sendOtpButton = findViewById(R.id.send_otp_btn)
        progressBar = findViewById(R.id.login_progress_bar)

        progressBar.visibility = View.GONE

        countryCodePicker.registerCarrierNumberEditText(phoneInput);
        sendOtpButton.setOnClickListener { v ->
            if (!countryCodePicker.isValidFullNumber) {
                phoneInput.setError("Phone Number is Not valid")
                return@setOnClickListener
            }

            val intent = Intent(this@LoginPhoneNumber, LoginOtp::class.java)
            intent.putExtra("phone", countryCodePicker.fullNumberWithPlus)
            startActivity(intent)
        }
    }
}