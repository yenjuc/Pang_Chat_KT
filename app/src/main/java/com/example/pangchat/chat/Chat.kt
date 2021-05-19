package com.example.pangchat.chat

class Chat(// 昵称
        private val nickname: String?, // 头像
        private val avatarIcon: Int, //最后聊天内容
        private val lastSpeak: String?, //最后联络时间
        private val lastSpeakTime: String?) {

    fun getAvatarIcon(): Int {
        return avatarIcon
    }

    fun getLastSpeak(): String? {
        return lastSpeak
    }

    fun getLastSpeakTime(): String? {
        return lastSpeakTime
    }

    fun getNickname(): String? {
        return nickname
    }

}