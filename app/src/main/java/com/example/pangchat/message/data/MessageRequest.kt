package com.example.pangchat.message.data

import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
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

data class MessageIdResp(
    val success: Boolean,
    val messageId: String
)

class MessageRequest {

    data class MessageId(val messageId: String)
    fun getMessage(messageId: String): MessageResult<MessageInfo> {
        val (_, _, result) = CookiedFuel.post("/message/info").jsonBody(MessageId(messageId)).responseObject<MessageInfo>()
        if (result is fuelResult.Failure) {
            return MessageResult.Error(result.getException())
        } else {
            return if (result.get().success)
                MessageResult.Success(result.get())
            else MessageResult.Error(Exception());
        }
    }

    data class MessageSend(val chatId: String, val nickname: String, val content: String)
    fun sendMessage(chatId: String, nickname: String, content: String): MessageResult<MessageIdResp>{
        val (_, _, result) = Fuel.post("/message/send").jsonBody(MessageSend(chatId, nickname, content)).responseObject<MessageIdResp>()
        if (result is fuelResult.Failure) {
            return MessageResult.Error(result.getException())
        } else {
            return if (result.get().success)
                MessageResult.Success(result.get())
            else MessageResult.Error(Exception());
        }
    }

    data class MessageIdAndUsername(val messageId: String, val username: String)
    fun recallMessage(messageId: String, username: String): MessageResult<MessageIdResp>{
        val (_, _, result) = Fuel.post("/message/recall").jsonBody(MessageIdAndUsername(messageId, username)).responseObject<MessageIdResp>()
        if (result is fuelResult.Failure) {
            return MessageResult.Error(result.getException())
        } else {
            return if (result.get().success)
                MessageResult.Success(result.get())
            else MessageResult.Error(Exception());
        }
    }
}