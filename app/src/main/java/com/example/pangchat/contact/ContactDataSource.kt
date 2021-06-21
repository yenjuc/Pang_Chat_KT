package com.example.pangchat.contact

import com.example.pangchat.fragment.data.Result
import com.example.pangchat.user.User
import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result as fuelResult

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

// 后端返回的数据格式
data class ContactInfo(
    val success: Boolean,
    val time: Long,
    val friendsInfo: ArrayList<User>,
    val newfriendsInfo: ArrayList<User>,
    )

data class AddFriendResult(
    var success: Boolean,
    val time: Long
)

data class IsFriendResult(
    var isFriend: Boolean,
    var success: Boolean,
    val time: Long
)

class ContactDataSource {

    // data class UserId(val userId: String?)

    fun getContactInfo(): Result<ContactInfo> {
        // val up = UserId(null)
        val (_, _, result) = CookiedFuel.post("/user/contact/info").responseObject<ContactInfo>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }


    data class NewFriend(val friendName: String)

    fun addNewFriend(friendName: String): Result<AddFriendResult> {
        val param = NewFriend(friendName)
        val (_, _, result) = CookiedFuel.post("/friend/add").jsonBody(param).responseObject<AddFriendResult>()

        if (result.get().success) {
            return Result.Success(result.get())
        }
        else {
            return Result.Error(Exception())
        }
    }

    data class NewFriendId(val friendId: String)

    fun acceptNewFriend(friendId: String): Result<AddFriendResult> {
        val param = NewFriendId(friendId)
        val (_, _, result) = CookiedFuel.post("/friend/accept").jsonBody(param).responseObject<AddFriendResult>()

        if (result.get().success) {
            return Result.Success(result.get())
        }
        else {
            return Result.Error(Exception())
        }
    }

    fun refuseNewFriend(friendId: String): Result<AddFriendResult> {
        val param = NewFriendId(friendId)
        val (_, _, result) = CookiedFuel.post("/friend/refuse").jsonBody(param).responseObject<AddFriendResult>()

        if (result.get().success) {
            return Result.Success(result.get())
        }
        else {
            return Result.Error(Exception())
        }
    }

    fun isFriend(friendId: String): Result<IsFriendResult> {
        val param = NewFriendId(friendId)
        val (_, _, result) = CookiedFuel.post("/friend/whether").jsonBody(param).responseObject<IsFriendResult>()

        if (result.get().success) {
            return Result.Success(result.get())
        }
        else {
            return Result.Error(Exception())
        }

    }

}