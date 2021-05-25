package com.example.pangchat.chat

class Chat(
        private val chatId: String,
        private val nickname: String?, // 昵称
        private val avatarIcon: Int,  // 头像
        private val lastSpeak: String?, //最后聊天内容
        private val lastSpeakTime: String? //最后联络时间)
){
    fun getChatId(): String{
        return chatId
    }

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