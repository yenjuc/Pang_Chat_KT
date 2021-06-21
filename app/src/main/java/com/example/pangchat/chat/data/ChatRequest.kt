package com.example.pangchat.chat.data

import com.example.pangchat.chat.Chat
import com.example.pangchat.message.Message
import com.example.pangchat.user.User
import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

/*
data class ChatInfo(
        val success: Boolean,
        val chatId: String,
        val records: ArrayList<String>,
        val isGroup: Boolean,
        val groupName: String,
        // val time: String
)

 */

data class ChatUserInfo(
    val success: Boolean,
    val chat: Chat,
    val members: ArrayList<User>
)

data class ChatMessageInfo(
    val success: Boolean,
    val chat: Chat,
    val records: ArrayList<Message>
)

data class ChatInfo(
    val success: Boolean,
    val chat: Chat
)

class ChatRequest {

    data class ChatId(val chatId: String)

    fun getChatAndMembers(chatId: String): ChatResult<ChatUserInfo> {
        val (_, _, result) = CookiedFuel.post("/chat/members").jsonBody(ChatId(chatId)).responseObject<ChatUserInfo>()
        if (result is fuelResult.Failure) {
            return ChatResult.Error(result.getException())
        } else {
            return if (result.get().success)
                ChatResult.Success(result.get())
            else ChatResult.Error(Exception());
        }
    }

    fun getMessagesOfChat(chatId: String) : ChatResult<ChatMessageInfo> {
        val (_, _, result) = CookiedFuel.post("/chat/messages").jsonBody(ChatId(chatId)).responseObject<ChatMessageInfo>()
        if (result is fuelResult.Failure) {
            return ChatResult.Error(result.getException())
        } else {
            return if (result.get().success)
                ChatResult.Success(result.get())
            else ChatResult.Error(Exception());
        }
    }

    data class UsernameAndMembers(val userId: String, val members: ArrayList<String>)
    fun newChat(userId: String, members: ArrayList<String>): ChatResult<ChatInfo> {
        val (_, _, result) = CookiedFuel.post("/chat/new").jsonBody(UsernameAndMembers(userId, members)).responseObject<ChatInfo>()
        if (result is fuelResult.Failure) {
            return ChatResult.Error(result.getException())
        } else {
            return if (result.get().success)
                ChatResult.Success(result.get())
            else ChatResult.Error(Exception());
        }
    }

}