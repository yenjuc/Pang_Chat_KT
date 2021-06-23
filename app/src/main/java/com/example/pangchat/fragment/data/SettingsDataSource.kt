package com.example.pangchat.fragment.data

import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

data class ModifyNicknameResult(
    val success: Boolean,
    val time: Long,
)

data class ModifyPasswordResult(
        val success: Boolean,
        val time: Long,
)

data class ModifyAvatarResult(
    val success: Boolean,
    val time: Long
)


class SettingsDataSource {

    data class NewNickName(val newNickname: String)

    fun modifyNickname(newNickname: String): Result<ModifyNicknameResult> {
        val up = NewNickName(newNickname)
        val (_, _, result) = CookiedFuel.post("/user/modify/nickname").jsonBody(up).responseObject<ModifyNicknameResult>()
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

    data class Avatar(val newAvatar: String)

    fun modifyAvatar(avatar: String): Result<ModifyAvatarResult> {
        val up = Avatar(newAvatar = avatar)
        val (_, _, result) = CookiedFuel.post("/user/modify/avatar").jsonBody(up).responseObject<ModifyAvatarResult>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }


}