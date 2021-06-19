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
import com.example.pangchat.message.data.MessageIdResp
import com.example.pangchat.message.data.MessageRequest
import com.example.pangchat.message.data.MessageResult
import com.example.pangchat.message.data.MessageInfo
import com.example.pangchat.websocketClient.webSocketClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ChatActivity : AppCompatActivity() {

    private val _messageInfo = MutableLiveData<MessageInfo>()

    private var chatInfo: ChatInfo? = null

    private var chatId: String? = null

    private var recyclerView: RecyclerView? = null

    private lateinit var messages:LinkedList<Message?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // TODO: 看看是否可拉取完再载入

        chatId = intent.getStringExtra("chatId")
        recyclerView = findViewById(R.id.chatRecyclerView)
        messages = LinkedList<Message?>()
        recyclerView?.adapter = MessageAdapter(webSocketClient.username!!, this, messages)
        // 取得对应聊天的内容
        lifecycleScope.launch {
            if (chatId != null) {
                getChatInfo(chatId!!)
                // 遍历某chat中的所有messageId
                for(i in 0 until chatInfo?.records!!.size) {
                    getMessageInfo(chatInfo?.records!![i])
                }
                recyclerView?.adapter?.notifyDataSetChanged()
                recyclerView?.scrollToPosition(messages.size - 1)
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

        // init()
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

        val chatInput = findViewById<TextInputEditText>(R.id.chatInput)

        // TODO: set 各种 listener
        val chatMoreAction = findViewById<LinearLayout>(R.id.chatMoreLayout)
        val chatAction = findViewById<ImageView>(R.id.chatAction)
        chatAction.setOnClickListener {
            if(chatInput.text?.isEmpty() == false){
                Log.d("Debug", "not empty")
                lifecycleScope.launch {
                    if (chatId != null) {
                        sendMessage(chatInput.text.toString())
                        recyclerView?.adapter?.notifyDataSetChanged()
                        recyclerView?.scrollToPosition(messages.size - 1)
                        chatInput.text?.clear()
                    }
                }
            }else{
                if(chatMoreAction.visibility == View.VISIBLE){
                    chatMoreAction.visibility = View.GONE
                }else if(chatMoreAction.visibility == View.GONE) {
                    chatMoreAction.visibility = View.VISIBLE
                }
            }
        }
    }

    public fun setInput(text: String){
        val chatInput = findViewById<TextInputEditText>(R.id.chatInput)
        chatInput.setText(text)
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


    // FIXME: nickname 动态取得
    private suspend fun sendMessage(content: String){
        val messageRequest = MessageRequest()
        val result: MessageResult<MessageIdResp>

        withContext(Dispatchers.IO) {
            result = messageRequest.sendMessage(chatId!!, webSocketClient.username!!, content)
        }

        if (result is MessageResult.Success) {
            // _messageInfo.value = result.data
                // FIXME: change to my data
            val message = Message(result.data.messageId, "my id", webSocketClient.username!!, "1", false, content, "time")
            messages.add(message)
        } else {
            // TODO：抛出并解析异常
        }
    }

    public fun recallMessage(index: Int, messageId: String, username: String){
        lifecycleScope.launch {
            if(recallMessage(messageId, username)){
                messages[index]?.setRecalled()
                recyclerView?.adapter?.notifyDataSetChanged()
            }
        }
    }

    public suspend fun recallMessage(messageId: String, username: String) : Boolean{
        val messageRequest = MessageRequest()
        val result: MessageResult<MessageIdResp>

        withContext(Dispatchers.IO) {
            result = messageRequest.recallMessage(messageId, username)
        }

        return result is MessageResult.Success
    }

}