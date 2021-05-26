package com.example.pangchat.discover

import com.example.pangchat.comment.Comment
import java.util.*
import kotlin.collections.ArrayList

class Discover(//昵称
        private val nickname: String?, //头像
        private val avatarIcon: Int, // 文字
        private val text: String?, // 发布时间
        private val publishedTime: String?, // 图片
        private val images: ArrayList<Int?>?,
        private var Likes:ArrayList<String>? = null,
        private val comments: LinkedList<Comment?>?=null
        ) {

    fun getNickname(): String? {
        return nickname
    }
    fun getComments(): LinkedList<Comment?>?{
        return comments
    }
    fun getAvatarIcon(): Int {
        return avatarIcon
    }

    fun getImages(): ArrayList<Int?>? {
        return images
    }

    fun getPublishedTime(): String? {
        return publishedTime
    }

    fun getText(): String? {
        return text
    }

    fun getImageCount(): Int {
        if (images != null) {
            return images.size
        }
        return 0
    }
    fun getLikes(): ArrayList<String>? {
        return Likes
    }
}