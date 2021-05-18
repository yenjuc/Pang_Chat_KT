package com.example.homework2.contact

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