package com.example.pangchat.contact

class Contact(
        private val userId: String,
        private val username: String,
        private val nickname: String,
        private val avatar: String) {

    fun getUserId(): String {
        return userId
    }

    fun getUsername(): String {
        return username
    }

    fun getAvatar(): String {
        return avatar
    }

    fun getNickname(): String {
        return nickname
    }

}