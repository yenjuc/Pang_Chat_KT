package com.example.pangchat.websocketClient

import com.alibaba.fastjson.JSON
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI

lateinit var webSocketURI: URI
lateinit var webSocketClient: FriendWebSocketClient

class FriendWebSocketClient(uri: URI) : WebSocketClient(uri) {
    var hasNewFriend : Boolean = false
    var username:String?=null
    var password:String?=null

    override fun onOpen(handshakedata: ServerHandshake?) {
        send(JSON.toJSONString(mapOf("bizType" to "USER_LOGIN", "username" to username, "password" to password)))
    }

    override fun onMessage(message: String?) {
        hasNewFriend = true
        print(message)
        // 在这里对业务类型做各种条件判断然后处理
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {
        print("123")
    }

    override fun onError(ex: Exception?) {
        print(ex)
    }


}