package com.example.pangchat.user.data

import com.example.pangchat.chat.Chat
import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

data class UserChats(
        val success: Boolean,
        val chats: ArrayList<Chat>
)

data class CommonResp(
    val success: Boolean
)

class UserRequest {

    data class Username(val userId: String)

    fun getUserChats(userId: String): UserResult<UserChats> {
        val (_, _, result) = CookiedFuel.post("/user/chats").jsonBody(Username(userId)).responseObject<UserChats>()
        if (result is fuelResult.Failure) {
            return UserResult.Error(result.getException())
        } else {
            return if (result.get().success)
                UserResult.Success(result.get())
            else UserResult.Error(Exception());
        }
    }

    data class userChatOp(val userId: ArrayList<String>, val chatId: String, val opType: String)
    fun userChat(userId: ArrayList<String>, chatId: String, opType: String): UserResult<CommonResp>{
        val (_, _, result) = CookiedFuel.post("/user/chat").jsonBody(userChatOp(userId, chatId, opType)).responseObject<CommonResp>()
        if (result is fuelResult.Failure) {
            return UserResult.Error(result.getException())
        } else {
            return if (result.get().success)
                UserResult.Success(result.get())
            else UserResult.Error(Exception());
        }
    }
}