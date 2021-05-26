package com.example.pangchat


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.chat.data.ChatInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.message.Message
import com.example.pangchat.message.MessageAdapter
import com.example.pangchat.message.data.MessageRequest
import com.example.pangchat.message.data.MessageResult
import com.example.pangchat.message.data.MessageInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ChatActivity : AppCompatActivity() {

    private val _messageInfo = MutableLiveData<MessageInfo>()

    private var chatInfo: ChatInfo? = null

    private lateinit var messages:LinkedList<Message?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val chatId = intent.getStringExtra("chatId")
        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)

        messages = LinkedList<Message?>()
        recyclerView?.adapter = MessageAdapter(messages)
        // 取得对应聊天的内容
        lifecycleScope.launch {
            if (chatId != null) {
                getChatInfo(chatId)
                // 遍历某chat中的所有messageId
                for(i in 0 until chatInfo?.records!!.size){
                    getMessageInfo(chatInfo?.records!![i])
                    recyclerView?.adapter?.notifyDataSetChanged()
                }
                runOnUiThread {
                    val chatname = findViewById<TextView>(R.id.chatName)
                    if(chatInfo!!.isGroup){
                        chatname.text = chatInfo!!.groupName
                    }else{
                        // FIXME: 应改成对方名称
                        chatname.text = "对方用户名"
                    }
                }
            }
        }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager

        click()
    }

    private fun click(){
        val back = findViewById<ImageView>(R.id.chatBackward)
        back.setOnClickListener { this.finish() }

        val chatinfo = findViewById<ImageView>(R.id.chatInfo)
        // TODO: 进入聊天室详情页面 activity
        chatinfo.setOnClickListener {
            val intent = Intent(this, ChatInfoActivity::class.java)
            try {
                startActivity(intent)
            } catch (ActivityNotFoundException: Exception) {
                Log.d("ImplicitIntents", "Can't handle this!")
            }
        }

        // TODO: set 各种 listener
        val chatMoreAction = findViewById<LinearLayout>(R.id.chatMoreLayout)
        val chatAction = findViewById<ImageView>(R.id.chatAction)
        chatAction.setOnClickListener {
            if(chatMoreAction.visibility == View.VISIBLE){
                chatMoreAction.visibility = View.GONE
            }else if(chatMoreAction.visibility == View.GONE) {
                chatMoreAction.visibility = View.VISIBLE
            }
        }
    }

    private suspend fun getChatInfo(chatId: String){
        val chatRequest = ChatRequest()
        val result: ChatResult<ChatInfo>

        withContext(Dispatchers.IO){
            result = chatRequest.getChat(chatId)
        }

        if (result is ChatResult.Success) {
            chatInfo = result.data
        } else {
            Log.d("error: ", "get chat info error")
            // TODO：抛出并解析异常
        }
    }

    private suspend fun getMessageInfo(messageId: String){
        val messageRequest = MessageRequest()
        val result: MessageResult<MessageInfo>

        withContext(Dispatchers.IO) {
            result = messageRequest.getMessage(messageId)
        }

        if (result is MessageResult.Success) {
            _messageInfo.value = result.data
            val message = Message(result.data.messageId, result.data.senderId, result.data.nickname, result.data.avatarIcon, result.data.recalled, result.data.content, result.data.time)
            messages.add(message)
        } else {
            // TODO：抛出并解析异常
        }
    }

}