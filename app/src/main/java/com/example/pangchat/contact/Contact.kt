package com.example.pangchat.contact

class Contact(// 昵称
    private val userId: String,
    private val nickname: String?, // 头像
    private val avatar: Int
) {


    fun getUserId(): String {
        return userId
    }


    fun getAvatar(): Int {
        return avatar
    }

    fun getNickname(): String? {
        return nickname
    }

}