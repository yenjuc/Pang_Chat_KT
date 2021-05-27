package com.example.pangchat.contact

class Contact(// 昵称
        private val userId: String,
        private val nickname: String?, // 头像
        private val avatarIcon: Int) {

    fun getUserId(): String {
        return userId
    }


    fun getAvatarIcon(): Int {
        return avatarIcon
    }

    fun getNickname(): String? {
        return nickname
    }

}