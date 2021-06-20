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

class UserRequest {

    data class Username(val username: String)

    fun getUserChats(username: String): UserResult<UserChats> {
        val (_, _, result) = CookiedFuel.post("/user/chats").jsonBody(Username(username)).responseObject<UserChats>()
        if (result is fuelResult.Failure) {
            return UserResult.Error(result.getException())
        } else {
            return if (result.get().success)
                UserResult.Success(result.get())
            else UserResult.Error(Exception());
        }

    }
}