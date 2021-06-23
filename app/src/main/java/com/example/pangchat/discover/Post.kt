package com.example.pangchat.discover

import com.example.pangchat.comment.Comment
import java.util.*
import kotlin.collections.ArrayList

class Post(//昵称
    private val id:String?,
    private val senderId:String?,
    private val nickname: String?,
    private val avatar: String,
    private val textContent: String?,
    private val postTime: String?,
    private val imagesList: ArrayList<String?>?=null,
    private var likesIdList:ArrayList<String?>? = null,
    private var Type:String = "image",
//        private val commentIdList: ArrayList<String?>?=null,
    private val commentList: LinkedList<Comment?>? = null,
    private var likesNicknameList: ArrayList<String?>? =null
        ) {

    fun getId():String?{
        return id;
    }
    fun getSenderId():String?{
        return senderId;
    }
    fun getNickname(): String? {
        return nickname
    }
//    fun getComments(): ArrayList<String?>?{
//        return commentIdList
//    }
    fun getType(): String {
        return Type
    }
    fun getComments(): LinkedList<Comment?>?{
        return commentList
    }
    fun getAvatarIcon(): String {
        return avatar
    }

    fun getImages(): ArrayList<String?>? {
        return imagesList
    }

    fun getPublishedTime(): String? {
        return postTime
    }

    fun getText(): String? {
        return textContent
    }

    fun getImageCount(): Int {
        if (imagesList != null) {
            return imagesList.size
        }
        return 0
    }
    fun getLikes(): ArrayList<String?>? {
        return likesNicknameList
    }
    fun getLikeIds(): ArrayList<String?>? {
        return likesIdList
    }
}