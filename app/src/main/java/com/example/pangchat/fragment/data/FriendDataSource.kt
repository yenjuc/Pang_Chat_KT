//package com.example.pangchat.fragment.data
//
//import android.os.Message
//import okhttp3.*
//import java.util.*
//
//
//public class ClientWebSocketListener : WebSocketListener() {
//    override fun onOpen(webSocket: WebSocket, response: Response) {
//        val mWebSocket = webSocket
//        mWebSocket.send("{'username':'pwf'}")
//    }
//
//    override fun onMessage(webSocket: WebSocket, text: String) {
//        val message: Message = Message.obtain()
//        message.obj = text
//        mWebSocketHandler.sendMessage(message)
//    }
//
//
//    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
//        var mWebSocket : WebSocket = webSocket
//        if (null != mWebSocket) {
//            mWebSocket.close(1000, "bye")
//            mWebSocket = null
//        }
//    }
//
//    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
//
//    }
//
//    override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
//
//    }
//}
//
//
//class FriendDataSource {
//    val listener = ClientWebSocketListener()
//    val request: Request = Request.Builder()
//            .url("ws://127.0.0.1:520/api")
//            .addHeader("Authorization", "Bearer gSGIgfdsosGIYgfds").build()
//    val client = OkHttpClient()
//    val websocket = client.newWebSocket(request, listener)
//
//
//}