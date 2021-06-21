package com.example.pangchat.user

class User(
        private val userId: String,
        private val username: String,
        private val avatar: String
)
{
    fun getUserId(): String {
        return userId;
    }

    fun getUsername(): String {
        return username;
    }

    fun getAvatar() : String {
        return avatar;
    }

}