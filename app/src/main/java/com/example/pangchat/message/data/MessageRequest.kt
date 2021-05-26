package com.example.pangchat.message.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import java.lang.Exception
import com.github.kittinunf.result.Result as fuelResult

data class MessageInfo(
        val success: Boolean,
        val messageId: String,
        val senderId: String,
        val nickname: String,
        val avatarIcon: String,
        val recalled: Boolean,
        val content: String,
        val time: String
)

class MessageRequest {

    data class MessageId(val messageId: String)

    fun getMessage(messageId: String): MessageResult<MessageInfo> {
        val (_, _, result) = Fuel.post("/message/info").jsonBody(MessageId(messageId)).responseObject<MessageInfo>()
        if (result is fuelResult.Failure) {
            return MessageResult.Error(result.getException())
        } else {
            return if (result.get().success)
                MessageResult.Success(result.get())
            else MessageResult.Error(Exception());
        }

    }
}