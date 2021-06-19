package com.example.pangchat.websocketClient

import com.alibaba.fastjson.JSON
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

// 全局变量，所有组件共享此websocket client
lateinit var webSocketURI: URI
lateinit var webSocketClient: MyWebSocketClient

class MyWebSocketClient(uri: URI) : WebSocketClient(uri) {
    // var hasNewFriend : Boolean = false
    var username:String?=null
    var password:String?=null

    override fun onOpen(handshakedata: ServerHandshake?) {
        send(JSON.toJSONString(mapOf("bizType" to "USER_LOGIN", "username" to username, "password" to password)))
    }

    override fun onMessage(message: String?) {
        // TODO:将message转为json类型，然后对其中的业务类型做各种条件判断， 分别处理
        // val qwq = Gson().fromJson(message, mutableMapOf<String, Any>().javaClass)
//        if (qwq[0]["bizType"] == "USER_ADD_FRIEND")
//        {
//            print(message)
//        }
        print(message)


    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        print("123")
    }

    override fun onError(ex: Exception?) {
        print(ex)
    }


}