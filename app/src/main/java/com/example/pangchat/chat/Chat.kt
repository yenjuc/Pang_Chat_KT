package com.example.pangchat.chat

class Chat(
        private val id: String,
        private val records: ArrayList<String>, // 存 message id
        private val members: ArrayList<String>, // 存 member username
        private val lastUpdateTime: String,     // 存最后对话时间
        private val lastUpdateConx: String,     // 存最后发信人和消息内容
        private val isGroup: Boolean,           // 存是否为群组
        private val groupMaster: String,        // 存群主名称
        private val chatAvatar: String,         // 存聊天头像
        private val chatName: String            // 存聊天名称
){
    fun getId(): String{
        return id
    }

    fun getRecords(): ArrayList<String>{
        return records
    }

    fun getMembers(): ArrayList<String>{
        return members
    }

    fun getLastUpdateTime(): String{
        return lastUpdateTime
    }

    fun getLastUpdateConx(): String{
        return lastUpdateConx
    }

    fun getIsGroup(): Boolean{
        return isGroup
    }

    fun getGroupMaster(): String{
        return groupMaster
    }

    fun getChatAvatar(): String{
        return chatAvatar
    }

    fun getChatName(): String{
        return chatName
    }
}