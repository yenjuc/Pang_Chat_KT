package com.example.pangchat.chat.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import java.lang.Exception
import kotlin.collections.ArrayList
import com.github.kittinunf.result.Result as fuelResult

data class ChatInfo(
        val success: Boolean,
        val chatId: String,
        val records: ArrayList<String>,
        val isGroup: Boolean,
        val groupName: String,
        // val time: String
)

class ChatRequest {

    data class ChatId(val chatId: String)

    fun getChat(chatId: String): ChatResult<ChatInfo> {
        val (_, _, result) = Fuel.post("/chat/info").jsonBody(ChatId(chatId)).responseObject<ChatInfo>()
        if (result is fuelResult.Failure) {
            return ChatResult.Error(result.getException())
        } else {
            return if (result.get().success)
                ChatResult.Success(result.get())
            else ChatResult.Error(Exception());
        }

    }
}