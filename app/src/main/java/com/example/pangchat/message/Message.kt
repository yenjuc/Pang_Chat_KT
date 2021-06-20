package com.example.pangchat.message

import java.util.*
import kotlin.collections.ArrayList

enum class MessageType{
    TEXT, IMAGE, VIDEO, FILE
}

// TODO: 确认传回参数
class Message(
    private val id: String,
    private val username: String,
    private val avatarIcon: String,
    private val blockedUser: ArrayList<String>,
    private var recalled: Boolean,
    private val content: String,
    private val type: String,
    private val time: String
        // private val messageType: MessageType
) {
    fun getId(): String{
        return id
    }

    fun getUsername(): String{
        return username
    }

    fun getAvatarIcon(): String{
        return avatarIcon
    }

    fun getBlockedUser(): ArrayList<String>{
        return blockedUser
    }

    // return true if the user has already delete the message
    fun isBlocked(username: String): Boolean{
        if(blockedUser != null){
            return blockedUser.contains(username)
        }
        return false
    }

    fun addBlocked(username: String) {
        blockedUser.add(username)
    }

    fun getRecalled(): Boolean{
        return recalled
    }

    fun setRecalled(){
        recalled = true
    }

    fun getContent(): String{
        return content
    }

    fun getType(): String{
        return type
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