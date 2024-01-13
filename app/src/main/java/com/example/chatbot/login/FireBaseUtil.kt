package com.example.chatbot.login


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore

class FireBaseUtil {



    companion object {
        fun currentUserId(): String {
            return FirebaseAuth.getInstance().currentUser?.uid ?: ""
        }


        fun currTopicName() : String{

            return "Topic_1"
        }

        fun isLoggedIn() : Boolean {
            if(currentUserId() != null){
                return true
            }
            return false
        }

        fun currentUserDetails(): DocumentReference {
            return FirebaseFirestore.getInstance().collection("users").document(currentUserId())
        }

        fun getChatroomReference( chatroomId : String, topicName : String) : DocumentReference{
            return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId).collection("Topic").document(topicName)
        }

        fun getChatroomMessageReference( chatroomId: String, topicName : String): CollectionReference{
            return getChatroomReference(chatroomId,topicName).collection("Chats")
        }

        fun allTopicCollection(chatroomId: String): CollectionReference {
            return FirebaseFirestore.getInstance().collection("chatrooms").document(chatroomId).collection("Topic")
        }

        fun getChatroomId (userId1 : String) : String
        {
            return userId1
        }
    }
}