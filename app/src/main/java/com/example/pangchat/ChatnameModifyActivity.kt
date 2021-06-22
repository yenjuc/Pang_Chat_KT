package com.example.pangchat


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.chat.data.ChatInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatnameModifyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatname_modify)

        val back = findViewById<ImageView>(R.id.chatnameModifyBackward)
        back.setOnClickListener {
            this.finish()
        }

        val chatname: String? = intent.getStringExtra("chatName")
        val input = findViewById<TextInputEditText>(R.id.chatnameInput)
        if(chatname != null){
            input.setText(chatname)
        }

        var chatId: String? = intent.getStringExtra("chatId")
        val btn = findViewById<Button>(R.id.chatnameModifyBtn)
        btn.setOnClickListener {
            if(input.text?.length != 0){
                modify(chatId!!, input.text.toString())
                val intent = Intent(this, ChatInfoActivity::class.java)
                intent.putExtra("chatId", chatId)
                intent.putExtra("chatName", input.text.toString())
                try {
                    startActivity(intent)
                    this.finish()
                } catch (ActivityNotFoundException: Exception) {
                    Log.d("ImplicitIntents", "Can't handle this!")
                }
            }else{
                Toast.makeText(this, "不可将群聊名置为空", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun modify(chatId: String, value: String){
        lifecycleScope.launch {
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
        } else {
            // TODO：抛出并解析异常
        }

        return result is ChatResult.Success
    }


}