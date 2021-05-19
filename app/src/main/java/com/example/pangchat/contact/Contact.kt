package com.example.pangchat.contact

class Contact(// 昵称
        private val nickname: String?, // 头像
        private val avatarIcon: Int) {

    fun getAvatarIcon(): Int {
        return avatarIcon
    }

    fun getNickname(): String? {
        return nickname
    }

}