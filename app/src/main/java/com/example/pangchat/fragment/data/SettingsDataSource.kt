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

    data class UserNewName(val newUsername: String)

    fun modifyUsername(newUsername: String): Result<ModifyUsernameResult> {
        val up = UserNewName(newUsername)
        val (_, _, result) = CookiedFuel.post("/user/modify/name").jsonBody(up).responseObject<ModifyUsernameResult>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

    data class UserOldNewPassword(val oldPassword: String, val newPassword: String)

    fun modifyPassword(oldPassword: String, newPassword: String): Result<ModifyPasswordResult> {
        val up = UserOldNewPassword(oldPassword, newPassword)
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