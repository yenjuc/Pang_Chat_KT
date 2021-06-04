package com.example.pangchat.fragment.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import com.github.kittinunf.result.Result as fuelResult

// 后端返回的数据格式
data class UserInfo(
    val success: Boolean,
    val time: Long,
    val username: String,
    val userId: String
)


class UserDataSource {
    data class UserId(val userId: String)
    fun getUserInfoById(userId: String) : Result<UserInfo> {
        val up = UserDataSource.UserId(userId)
        val (_, _, result) = Fuel.post("/user/info").jsonBody(up).responseObject<UserInfo>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }
}