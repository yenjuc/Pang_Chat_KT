package com.example.pangchat.contact

import com.example.pangchat.fragment.data.Result

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.google.gson.Gson
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList
import com.github.kittinunf.result.Result as fuelResult

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */

// 后端返回的数据格式
data class ContactInfo(
        val success: Boolean,
        val time: Long,
        val friendsId: ArrayList<String>,
        val friendsName: ArrayList<String>
)

class ContactDataSource {

    data class UserId(val userId: String)

    fun getContactInfo(userId: String): Result<ContactInfo> {
        val up = UserId(userId)
        val (_, _, result) = Fuel.post("/user/contact/info").jsonBody(up).responseObject<ContactInfo>()
        if (result is fuelResult.Failure) {
            return Result.Error(result.getException())
        } else {
            return if (result.get().success)
                Result.Success(result.get())
            else Result.Error(Exception());
        }

    }

}