package com.example.pangchat.message.data

import com.example.pangchat.message.Message
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

data class MessageResp(
    val success: Boolean,
    val message: Message
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

    data class MessageSend(val chatId: String, val senderId: String, val type: String, val content: String)
    fun sendMessage(chatId: String, senderId: String, type: String, content: String): MessageResult<MessageResp>{
        val (_, _, result) = CookiedFuel.post("/message/send").jsonBody(MessageSend(chatId, senderId, type, content)).responseObject<MessageResp>()
        if (result is fuelResult.Failure) {
            return MessageResult.Error(result.getException())
        } else {
            return if (result.get().success)
                MessageResult.Success(result.get())
            else MessageResult.Error(Exception());
        }
    }

    data class MessageIdAndUsername(val messageId: String)
    fun recallMessage(messageId: String): MessageResult<MessageResp>{
        val (_, _, result) = CookiedFuel.post("/message/recall").jsonBody(MessageIdAndUsername(messageId)).responseObject<MessageResp>()
        if (result is fuelResult.Failure) {
            return MessageResult.Error(result.getException())
        } else {
            return if (result.get().success)
                MessageResult.Success(result.get())
            else MessageResult.Error(Exception());
        }
    }
}