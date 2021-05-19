package com.example.pangchat.discover

import java.util.*

class Discover(//昵称
        private val nickname: String?, //头像
        private val avatarIcon: Int, // 文字
        private val text: String?, // 发布时间
        private val publishedTime: String?, // 图片
        private val images: ArrayList<Int?>?) {

    fun getNickname(): String? {
        return nickname
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
}