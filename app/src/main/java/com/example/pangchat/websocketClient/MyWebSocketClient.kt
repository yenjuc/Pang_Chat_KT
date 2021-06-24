package com.example.pangchat.websocketClient

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.alibaba.fastjson.JSON
import com.example.pangchat.ChatActivity
import com.example.pangchat.ChatsFragment
import com.example.pangchat.MainActivity
import com.example.pangchat.R
import com.example.pangchat.message.Message
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.sql.Time

// 全局变量，所有组件共享此websocket client
lateinit var webSocketURI: URI
lateinit var webSocketClient: MyWebSocketClient

class MyWebSocketClient(uri: URI) : WebSocketClient(uri) {
    // var hasNewFriend : Boolean = false
    var userId:String? = null
    var username:String?=null
    var password:String?=null
    var urlToBitmap: MutableMap<String, Bitmap> = mutableMapOf()
    lateinit var context: Context


    override fun onOpen(handshakedata: ServerHandshake?) {
        send(JSON.toJSONString(mapOf("bizType" to "USER_LOGIN", "username" to username, "password" to password)))
    }

    override fun onMessage(message: String?) {
        // 将message转为json类型，然后对其中的业务类型做各种条件判断
        var arr: MutableList<MutableMap<String, *>>
        try {
            @Suppress("UNCHECKED_CAST")
            arr = Gson().fromJson(message, MutableList::class.java) as MutableList<MutableMap<String, *>>
        } catch (e:JsonSyntaxException){
            val obj = Gson().fromJson(message, MutableMap::class.java)
            @Suppress("UNCHECKED_CAST")
            arr = mutableListOf(obj as MutableMap<String, *>)
        }
        for (obj in arr) {

            // 有新的好友申请
            if (obj.get("bizType") == "USER_ADD_FRIEND") {
                val name: String = obj.get("friendName") as String
                val Id: String = obj.get("friendId") as String
                sendSimpleNotification("新好友提醒", "$name 请求添加你为好友")
            }
            // 有新的消息接收
            else if (obj.get("bizType") == "MESSAGE_SEND") {
                val chatId: String = obj.get("chatId") as String
                val id: String = obj.get("id") as String
                val senderId: String = obj.get("senderId") as String
                val username: String = obj.get("username") as String
                val avatar: String = obj.get("avatar") as String
                val content: String = obj.get("content") as String
                val type: String = obj.get("type") as String
                val timeStp: String = obj.get("timeStp") as String
                if(context is ChatActivity){
                    (context as ChatActivity).addMessageWebSocket(chatId, Message(id, senderId, username,
                        avatar, arrayListOf(),false, content, type, timeStp))
                }
                else if(context is MainActivity){
                    ((context as MainActivity).chatsFragment as ChatsFragment).updateChats()
                }
                sendSimpleNotification("新消息提醒", "$username 发送了一条消息")
            }
            else if (obj.get("bizType") == "USER_ACCEPT_FRIEND") {
                val name: String = obj.get("username") as String
                val Id: String = obj.get("userId") as String
                sendSimpleNotification("好友通过提醒", "你已和 $name 成为好友，快去聊天吧！")
            }
            else if (obj.get("bizType") == "MESSAGE_RECALL"){
                val chatId: String = obj.get("chatId") as String
                val messageId: String = obj.get("messageId") as String
                if(context is ChatActivity){
                    (context as ChatActivity).messageRecalled(chatId, messageId)
                }
                else if(context is MainActivity){
                    ((context as MainActivity).chatsFragment as ChatsFragment).updateChats()
                }
                sendSimpleNotification("消息撤回提醒", "[有消息被撤回]")
            }
        }


    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        print("123")
    }

    override fun onError(ex: Exception?) {
        print(ex)
    }


    fun sendSimpleNotification(title: String, text: String) {
        val manager: NotificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val channelId: String = "pangchat";
        val channel = NotificationChannel(channelId,"pangchat",
            NotificationManager.IMPORTANCE_DEFAULT);
        manager.createNotificationChannel(channel);
        val notification: Notification = NotificationCompat.Builder(context,channelId)
            .setContentTitle(title)
            .setContentText(text)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.avatar1)
            .build();
        manager.notify(1,notification);
    }

}