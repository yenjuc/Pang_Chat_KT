package com.example.pangchat.fragment.data

import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

// 后端返回的数据格式
data class UserInfo(
    val success: Boolean,
    val time: Long,
    val username: String,
    val nickname: String,
    val userId: String,
    val avatar: String
)


class UserDataSource {
    data class Username(val queryUsername: String)

    fun getUserInfoByName(username: String) : Result<UserInfo> {
        val up = Username(username)
        val (_, _, result) = CookiedFuel.post("/user/info").jsonBody(up).responseObject<UserInfo>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }


}