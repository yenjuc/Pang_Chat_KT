package com.example.pangchat.comment

class Comment(
    private val id: String,
    private val senderId: String,
    private val receiverId: String,
    private val content: String,
    private val senderNickname: String,
    private val receiverNickname: String,
) {
    fun getNickname():String{
        return senderNickname;
    }
    fun getText():String{
        return content;
    }
}