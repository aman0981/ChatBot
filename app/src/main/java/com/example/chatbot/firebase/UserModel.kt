package com.example.chatbot.firebase

import com.google.firebase.Timestamp

class UserModel {

    private lateinit var phone: String
    private lateinit var username: String
    private lateinit var createdTimestamp: Timestamp
    private lateinit var userId: String

    constructor()

    constructor(phone: String, username: String, createdTimestamp: Timestamp, userId: String) {
        this.phone = phone
        this.username = username
        this.createdTimestamp = createdTimestamp
        this.userId = userId
    }

    // Getter and Setter for 'phone' property
    var getPhone: String
        get() = phone
        set(value) {
            phone = value
        }

    // Getter and Setter for 'username' property
    var getUsername: String
        get() = username
        set(value) {
            username = value
        }

    // Getter and Setter for 'createdTimestamp' property
    var getCreatedTimestamp: Timestamp
        get() = createdTimestamp
        set(value) {
            createdTimestamp = value
        }

    // Getter and Setter for 'userId' property
    var getUserId: String
        get() = userId
        set(value) {
            userId = value

        }

}