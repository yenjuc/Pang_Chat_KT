package com.example.pangchat.contact

class Contact(
        private val userId: String,
        private val username: String,
        private val nickname: String,
        private val avatar: Int) {

    fun getUserId(): String {
        return userId
    }

    fun getUsername(): String {
        return username
    }

    fun getAvatar(): Int {
        return avatar
    }

    fun getNickname(): String {
        return nickname
    }

}