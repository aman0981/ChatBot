package com.example.chatbot.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import com.example.chatbot.MainActivity
import com.example.chatbot.R
import com.google.firebase.Timestamp

class LoginUserName : AppCompatActivity() {

    lateinit var userModel: UserModel
    lateinit var startChat : Button
    lateinit var usernameInput : EditText
    lateinit var progressBar : ProgressBar
    lateinit var phoneNumber: String
    var previousPhoneNumber:String= ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_username)


        startChat = findViewById(R.id.start_chat_btn)
        usernameInput = findViewById(R.id.login_username)
        progressBar = findViewById(R.id.login_progress_bar)

        phoneNumber = getIntent().getExtras()?.getString("phone").toString()


//        if(previousPhoneNumber == phoneNumber) {
//            getUsername()
//        }

        startChat.setOnClickListener(){
            //previousPhoneNumber = phoneNumber
            setUsername()
        }


    }

    fun setUsername(){

        val username = usernameInput.getText().toString();
        if(username.isEmpty() || username.length<3){
            usernameInput.setError("Username length should be at least 3 chars")
            return
        }

        setInProgress(true)

        if(::userModel.isInitialized){
            userModel.getUsername = username
        }else{
            userModel = UserModel(phoneNumber,username, Timestamp.now(), FireBaseUtil.currentUserId())
        }

        FireBaseUtil.currentUserDetails().set(userModel).addOnCompleteListener{ task->
            setInProgress(false)
            if(task.isSuccessful){
                val intent = Intent(this@LoginUserName, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            }
        }
    }


    fun getUsername() {
        setInProgress(true)
        FireBaseUtil.currentUserDetails().get().addOnCompleteListener{task->
            setInProgress(false)
            if(task.isSuccessful){
                userModel= task.getResult().toObject(UserModel::class.java)!!
                if(userModel !=null){
                    usernameInput.setText(userModel.getUsername)
                }
            }
        }
    }

    fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            progressBar.visibility = View.VISIBLE
            startChat.visibility = View.GONE
        } else {
            progressBar.visibility = View.GONE
            startChat.visibility = View.VISIBLE
        }
    }
}