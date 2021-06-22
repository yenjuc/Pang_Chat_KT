package com.example.pangchat.user

class User(
        private val userId: String,
        private val username: String,
        private val nickname: String,
        private val avatar: String
)
{
    fun getUserId(): String {
        return userId;
    }

    fun getUsername(): String {
        return username;
    }

    fun getNickname(): String {
        return nickname;
    }

    fun getAvatar() : String {
        return avatar;
    }

}