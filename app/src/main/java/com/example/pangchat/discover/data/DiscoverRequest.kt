package com.example.pangchat.discover.data

import com.example.pangchat.message.data.MessageInfo
import com.example.pangchat.message.data.MessageRequest
import com.example.pangchat.message.data.MessageResult
import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result

data class sendPostResult(
    val success: Boolean,
    val time: Long,
)

class DiscoverRequest {
    data class postSend(val senderId:String, val images:ArrayList<String>, val content:String)
    fun sendPost(senderId:String, images:ArrayList<String>, content:String):DiscoverResult<sendPostResult>{
        val (_, _, result) = CookiedFuel.post("/post/new").jsonBody(
          postSend(senderId, images,content)).responseObject<sendPostResult>()
        if (result is Result.Failure) {
            return DiscoverResult.Error(result.getException())
        } else {
            return if (result.get().success)
                DiscoverResult.Success(result.get())
            else DiscoverResult.Error(Exception());
        }
    }
}