package com.example.pangchat.fragment.data

import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.MyWebSocketClient
import com.example.pangchat.websocketClient.webSocketClient
import com.example.pangchat.websocketClient.webSocketURI
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

// 后端返回的数据格式
data class LoggedInUser(
        val success: Boolean,
        // val time: Long,
        val username: String,
        val userId: String
)


class LoginDataSource {

    data class UsernameAndPassword(val username: String, val password: String)

    fun login(username: String, password: String): Result<LoggedInUser> {
        val up = UsernameAndPassword(username, password)
        val (_, _, result) = CookiedFuel.post("/user/login").jsonBody(up).responseObject<LoggedInUser>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            webSocketClient = MyWebSocketClient(webSocketURI)
            webSocketClient.username = username
            webSocketClient.password = password
            webSocketClient.userId = result.get().userId
            webSocketClient.connect()
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

    fun logout() {
        // TODO: revoke authentication
    }
}