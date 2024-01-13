package com.example.chatbot.chathistory

import com.google.firebase.Timestamp

class ChatMessageModel {
    private lateinit var message: String
    private lateinit var senderId: String
    private lateinit var timestamp: Timestamp

    constructor()

    constructor(message: String, senderId: String, timestamp: Timestamp) {
        this.message = message
        this.senderId = senderId
        this.timestamp = timestamp
    }

    var getmessage : String
        get() =  message
        set(value){
            message = value
        }

    var getsenderId : String
        get()  = senderId
        set(value) {
            senderId = value
        }

    var gettimestamp : Timestamp
        get() = timestamp
        set(value) {
            timestamp = value
        }

}