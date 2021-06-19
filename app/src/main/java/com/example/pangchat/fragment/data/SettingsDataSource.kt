package com.example.pangchat.fragment.data

import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

data class ModifyUsernameResult(
    val success: Boolean,
    val time: Long,
)

data class ModifyPasswordResult(
        val success: Boolean,
        val time: Long,
)


class SettingsDataSource {

    data class UserIdAndNewName(val userId: String, val newUsername: String)

    fun modifyUsername(userId: String, newUsername: String): Result<ModifyUsernameResult> {
        val up = UserIdAndNewName(userId, newUsername)
        val (_, _, result) = CookiedFuel.post("/user/modify/name").jsonBody(up).responseObject<ModifyUsernameResult>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

    data class UserIdAndOldNewPassword(val userId: String, val oldPassword: String, val newPassword: String)

    fun modifyPassword(userId: String, oldPassword: String, newPassword: String): Result<ModifyPasswordResult> {
        val up = UserIdAndOldNewPassword(userId, oldPassword, newPassword)
        val (_, _, result) = CookiedFuel.post("/user/modify/password").jsonBody(up).responseObject<ModifyPasswordResult>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

}