package com.example.pangchat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.chat.data.ChatInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.websocketClient.webSocketClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatnameModifyActivity : AppCompatActivity() {

    private var chatname: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatname_modify)

        val back = findViewById<ImageView>(R.id.chatnameModifyBackward)
        back.setOnClickListener {
            this.finish()
        }

        chatname = intent.getStringExtra("chatName")
        val input = findViewById<TextInputEditText>(R.id.chatnameInput)
        if(chatname != null){
            input.setText(chatname)
        }

        var chatId: String? = intent.getStringExtra("chatId")
        val btn = findViewById<Button>(R.id.chatnameModifyBtn)
        btn.setOnClickListener {
            modify(chatId!!, input.text.toString())
            var intent = Intent()
            intent.putExtra("chatId", chatId)
            intent.putExtra("chatName", input.text.toString())
            setResult(RESULT_OK, intent)
            this.finish()
        }
    }

    override fun onResume() {
        super.onResume()
        webSocketClient.context = this
    }

    private fun modify(chatId: String, value: String){
        MainScope().launch {
            modifyChatname(chatId, value)
        }
    }

    private suspend fun modifyChatname(chatId: String, value: String): Boolean{
        val chatRequest = ChatRequest()
        val result: ChatResult<ChatInfo>

        withContext(Dispatchers.IO) {
            result = chatRequest.chatModify(chatId, "chatName",value)
        }

        if (result is ChatResult.Success) {
            Log.d("chat", "success")
            chatname = value
        } else {
            // TODO：抛出并解析异常
        }

        return result is ChatResult.Success
    }


}