package com.example.pangchat.message

import java.util.*

enum class MessageType{
    TEXT, IMAGE, VIDEO, FILE
}

// TODO: 确认传回参数
class Message(
        private val messageId: String,
        private val senderId: String,
        // private val blockedUser: LinkedList<Int>,
        private val nickname: String,
        private val avatarIcon: String,
        private val recalled: Boolean,
        private val content: String,
        private val time: String
        // private val messageType: MessageType
) {
    fun getMessageId(): String{
        return messageId
    }

    fun getSenderId(): String{
        return senderId
    }

    fun getNickname(): String{
        return nickname
    }

    fun getAvatarIcon(): String{
        return avatarIcon
    }

    fun getRecalled(): Boolean{
        return recalled
    }

    fun getContent(): String{
        return content
    }

    fun getTime(): String{
        return time
    }

    /*
    fun getMessageType(): MessageType{
        return messageType
    }

     */
}