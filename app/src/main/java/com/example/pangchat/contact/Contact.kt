package com.example.pangchat.contact

class Contact(// 昵称
        private val contactId: String,
        private val nickname: String?, // 头像
        private val avatarIcon: Int) {

    fun getContactId(): String {
        return contactId
    }


    fun getAvatarIcon(): Int {
        return avatarIcon
    }

    fun getNickname(): String? {
        return nickname
    }

}