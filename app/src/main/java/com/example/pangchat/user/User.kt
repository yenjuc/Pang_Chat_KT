package com.example.pangchat.user

import com.example.pangchat.message.Message

class User(
        private val username: String,
        private val avatar: String
){
    fun getUsername(): String {
        return username;
    }

    fun getAvatar() : String {
        return avatar;
    }

}