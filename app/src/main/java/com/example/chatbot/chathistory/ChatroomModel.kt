package com.example.chatbot.chathistory

import com.google.firebase.Timestamp


class ChatroomModel {
    lateinit var topicName: String
    lateinit var chatroomId: String
//    lateinit var lastMessage:String
    lateinit var userIds : List<String>
    lateinit var lastMessageTimestamp : Timestamp
   // lateinit var lastMessageSenderId : String

    constructor()

    constructor(topicName : String , chatroomId: String,  userIds: List<String>, lastMessageTimestamp: Timestamp) {
        this.topicName = topicName
        this.chatroomId = chatroomId
        this.userIds = userIds
        this.lastMessageTimestamp = lastMessageTimestamp
       // this.lastMessageSenderId = lastMessageSenderId
    }


//    var getchatroomId : String
//        get()= chatroomId
//        set(value) {
//            chatroomId= value
//        }

//    var getuserIds : List<String>
//        get() = userIds
//        set(value) {
//            userIds = value
//        }

    var getlastMessageTimestamp : Timestamp
        get() = lastMessageTimestamp
        set(value) {
            lastMessageTimestamp = value
        }

//    var getlastMessage : String
//        get() = lastMessage
//        set(value) {
//            lastMessage = value
//        }

//    var getlastMessageSenderId : String
//        get() = lastMessageSenderId
//        set(value) {
//            lastMessageSenderId = value
//        }
}