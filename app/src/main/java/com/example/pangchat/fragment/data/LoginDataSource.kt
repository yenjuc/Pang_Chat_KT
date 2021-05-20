package com.example.pangchat.fragment.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import com.github.kittinunf.result.Result as fuelResult

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

// 后端返回的数据格式
data class LoggedInUser(
        val success: Boolean,
        val time: Long,
        val username: String,
        val userId: String
)


class LoginDataSource {

    data class UsernameAndPassword(val username: String, val password: String)

    fun login(username: String, password: String): Result<LoggedInUser> {
        val up = UsernameAndPassword(username, password)
        val (_, _, result) = Fuel.post("/user/login").jsonBody(up).responseObject<LoggedInUser>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

    fun logout() {
        // TODO: revoke authentication
    }
}