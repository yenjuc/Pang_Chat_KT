package com.example.pangchat.discover.data

import com.example.pangchat.discover.Post
import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.gson.jsonBody
import com.github.kittinunf.fuel.gson.responseObject
import com.github.kittinunf.result.Result
import java.util.*
import kotlin.collections.ArrayList


data class sendPostResult(
    val success: Boolean,
    val time: Long,
)

data class postGetAllResult(
    val success: Boolean,
    val time: Long,
    var posts: LinkedList<Post?>
    )

data class postLikeResult(
    val success: Boolean,
    val time: Long,
)

data class postCommentResult(
    val success: Boolean,
    val time: Long,
)

data class cancelLikeResult(
    val success: Boolean,
    val time: Long,
)

class DiscoverRequest {
    data class postSend(val senderId: String, val images: ArrayList<String>, val content: String,val Type:String)
    fun sendPost(senderId: String, images: ArrayList<String>, content: String,type:String):DiscoverResult<sendPostResult>{
        val (_, _, result) = CookiedFuel.post("/post/new").jsonBody(
            postSend(senderId, images, content,type)
        ).responseObject<sendPostResult>()
        if (result is Result.Failure) {
            return DiscoverResult.Error(result.getException())
        } else {
            return if (result.get().success)
                DiscoverResult.Success(result.get())
            else DiscoverResult.Error(Exception());
        }
    }

    data class postGetAll(val userId: String)
    fun getAllPosts(userId: String):DiscoverResult<postGetAllResult>{
        val (_, _, resultqwq) = CookiedFuel.post("/post/getAll").jsonBody(
            postGetAll(userId)
        ).responseString()
        val (_, _, result) = CookiedFuel.post("/post/getAll").jsonBody(
            postGetAll(userId)
        ).responseObject<postGetAllResult>()
        if (result is Result.Failure) {
            return DiscoverResult.Error(result.getException())
        } else {
            return if (result.get().success)
                DiscoverResult.Success(result.get())
            else DiscoverResult.Error(Exception());
        }
    }

    data class postLike(val userId:String,val postId:String)
    fun likePost(userId: String,postId: String):DiscoverResult<postLikeResult>{
        val (_, _, result) = CookiedFuel.post("/post/like").jsonBody(
            postLike(userId,postId)
        ).responseObject<postLikeResult>()
        if (result is Result.Failure) {
            return DiscoverResult.Error(result.getException())
        } else {
            return if (result.get().success)
                DiscoverResult.Success(result.get())
            else DiscoverResult.Error(Exception());
        }
    }

    data class cancelLike(val userId:String,val postId:String)
    fun cancelLikePost(userId: String,postId: String):DiscoverResult<cancelLikeResult>{
        val (_, _, result) = CookiedFuel.post("/post/cancelLike").jsonBody(
            cancelLike(userId,postId)
        ).responseObject<cancelLikeResult>()
        if (result is Result.Failure) {
            return DiscoverResult.Error(result.getException())
        } else {
            return if (result.get().success)
                DiscoverResult.Success(result.get())
            else DiscoverResult.Error(Exception());
        }
    }

    data class postComment(val postId: String,val senderId: String,val content: String)
    fun commentPost(postId: String,senderId: String,content: String):DiscoverResult<postCommentResult>{
        val (_, _, result) = CookiedFuel.post("/post/comment").jsonBody(
            postComment(postId,senderId,content)
        ).responseObject<postCommentResult>()
        if (result is Result.Failure) {
            return DiscoverResult.Error(result.getException())
        } else {
            return if (result.get().success)
                DiscoverResult.Success(result.get())
            else DiscoverResult.Error(Exception());
        }
    }

}