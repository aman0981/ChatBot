package com.example.chatbot.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.example.chatbot.R
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Timer
import java.util.TimerTask
import java.util.concurrent.TimeUnit

class LoginOtp : AppCompatActivity() {

    lateinit var phoneNumber: String
    var timeoutSeconds: Long = 60L
    lateinit var verificationCode: String
    lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken

    lateinit var otpInput: EditText
    lateinit var Next_btn: Button
    lateinit var progressBar: ProgressBar
    lateinit var resendOtpTextView: TextView

    val mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_otp)

        otpInput = findViewById(R.id.login_otp)
        Next_btn = findViewById(R.id.next_btn)
        progressBar = findViewById(R.id.login_progress_bar)
        resendOtpTextView = findViewById(R.id.resend_otp_textview)


        phoneNumber = getIntent().getExtras()?.getString("phone").toString()


        sendOtp(phoneNumber,false)

        Next_btn.setOnClickListener {
            // Implementation for onClick

            val enterOtp: String = otpInput.getText().toString()
            val credential: PhoneAuthCredential =
                PhoneAuthProvider.getCredential(verificationCode, enterOtp)
            signIn(credential)
        }

        resendOtpTextView.setOnClickListener{
            sendOtp(phoneNumber, true)
        }


    }

    fun sendOtp(phoneNumber: String, isResend: Boolean) {
        startResendTimer()
        setInProgress(true)
        val builder: PhoneAuthOptions.Builder =
            PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phoneNumber)
                .setTimeout(timeoutSeconds, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                        signIn(phoneAuthCredential)
                        setInProgress(false)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        AndroidUtil.showToast(applicationContext, "OTP verification failed")
                        setInProgress(false)
                    }

                    override fun onCodeSent(
                        s: String, forceResendingToken: PhoneAuthProvider.ForceResendingToken,
                    ) {
                        super.onCodeSent(s, forceResendingToken)
                        verificationCode = s
                        resendingToken = forceResendingToken
                        AndroidUtil.showToast(applicationContext, "OTP sent Successfully")
                        setInProgress(false)
                    }

                })

        if (isResend) {
            PhoneAuthProvider.verifyPhoneNumber(
                builder.setForceResendingToken(resendingToken).build()
            )
        } else {
            PhoneAuthProvider.verifyPhoneNumber(builder.build())
        }

    }



    fun signIn(phoneAuthCredential: PhoneAuthCredential) {

        setInProgress(true)
        mAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@LoginOtp, LoginUserName::class.java)
                intent.putExtra("phone", phoneNumber)
                startActivity(intent)
            } else {
                AndroidUtil.showToast(applicationContext, "OTP Verification Failed")
            }
        }

    }


    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.visibility = View.VISIBLE
            Next_btn.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            Next_btn.visibility = View.VISIBLE
        }
    }

    fun startResendTimer() {
        resendOtpTextView.isEnabled = false
        val timer = Timer()
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                timeoutSeconds--
                runOnUiThread {
                    resendOtpTextView.text = "Resend Otp in $timeoutSeconds seconds"
                }
                if (timeoutSeconds <= 0) {
                    timeoutSeconds = 60L
                    timer.cancel()
                    runOnUiThread {
                        resendOtpTextView.isEnabled = true
                    }
                }
            }
        }, 0, 1000)
    }
}