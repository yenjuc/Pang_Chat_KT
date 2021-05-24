package com.example.pangchat.fragment.data

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import com.github.kittinunf.result.Result as fuelResult

data class ModifyUsernameResult(
    val success: Boolean,
    val time: Long,
)

class SettingsDataSource {

    data class UserIdAndNewName(val userId: String, val newUsername: String)

    fun modifyUsername(userId: String, newUsername: String): Result<ModifyUsernameResult> {
        val up = UserIdAndNewName(userId, newUsername)
        val (_, _, result) = Fuel.post("/user/modify/name").jsonBody(up).responseObject<ModifyUsernameResult>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

}