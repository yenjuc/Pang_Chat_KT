package com.example.pangchat.chat.data

import com.example.pangchat.chat.Chat
import com.example.pangchat.message.Message
import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

data class ChatInfo(
        val success: Boolean,
        val chatId: String,
        val records: ArrayList<String>,
        val isGroup: Boolean,
        val groupName: String,
        // val time: String
)

data class ChatMessageInfo(
    val success: Boolean,
    val chat: Chat,
    val records: ArrayList<Message>
)

class ChatRequest {

    data class ChatId(val chatId: String)
    fun getChat(chatId: String): ChatResult<ChatInfo> {
        val (_, _, result) = CookiedFuel.post("/chat/info").jsonBody(ChatId(chatId)).responseObject<ChatInfo>()
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
}