package com.example.pangchat.websocketClient

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Bitmap
import androidx.core.app.NotificationCompat
import com.alibaba.fastjson.JSON
import com.example.pangchat.R
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

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

    // data class friend(val friendId: String, val friendName: String)


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
            else if (obj.get("bizType") == "MESSAGE_SEND") {
                val name: String = obj.get("senderName") as String
                val Id: String = obj.get("senderId") as String
                sendSimpleNotification("新消息提醒", "$name 发送了一条消息")
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