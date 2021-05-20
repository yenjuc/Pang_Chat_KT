package com.example.pangchat.message

import java.util.*

enum class MessageType{
    TEXT, IMAGE, VIDEO, FILE
}

// TODO: 确认传回参数
class Message(
        private val messageId: Int,
        private val senderId: Int,
        private val blockedUser: LinkedList<Int>,
        private val recalled: Boolean,
        private val content: String,
        private val messageType: MessageType
) {
    fun getMessageId(): Int{
        return messageId
    }

    fun getSenderId(): Int{
        return senderId
    }

    fun getRecalled(): Boolean{
        return recalled
    }

    fun getContent(): String{
        return content
    }

    fun getMessageType(): MessageType{
        return messageType
    }
}