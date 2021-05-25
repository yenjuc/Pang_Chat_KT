package com.example.pangchat


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.message.data.Result
import com.example.pangchat.message.Message
import com.example.pangchat.message.MessageAdapter
import com.example.pangchat.message.data.MessageInfo
import com.example.pangchat.message.data.MessageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ChatActivity : AppCompatActivity() {

    private val _messageInfo = MutableLiveData<MessageInfo>()

    private lateinit var messages:LinkedList<Message?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // FIXME: 应改成 chatId
        val messageId = intent.getStringExtra("messageId")

        // FIXME: 遍历某chat中的所有messageId
        val messageIds = LinkedList<String>()
        messageIds.add("60abcaee1695b922b065328c")
        messageIds.add("60abcd4a1695b922b065328d")
        messageIds.add("60a68b9085acff97802413c5")
        messageIds.add("60a68b9085acff97802413c6")

        /*
        messageIds.add("0")
        messageIds.add("1")
        messageIds.add("2")
        messageIds.add("3")
        messageIds.add("4")
        messageIds.add("5")

         */



        val recyclerView = findViewById<RecyclerView>(R.id.chatRecyclerView)

        messages = LinkedList<Message?>()

        recyclerView?.adapter = MessageAdapter(messages);

        for(i in 0 until messageIds.size){
            lifecycleScope.launch {

                // messages.clear()
                messageIds[i]?.let { getMessageInfo(it) }
                // messageIds.sortBy { time }
                recyclerView?.adapter?.notifyDataSetChanged()
            }
        }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager

        click()
    }

    fun click(){
        val back = findViewById<ImageView>(R.id.chatBackward)
        back.setOnClickListener { this.finish() }

        val chatname = findViewById<TextView>(R.id.chatName)
        // TODO: 设置聊天室名称为聊天室名(由intent取得)
        chatname.text = "学习的胖"

        val chatinfo = findViewById<ImageView>(R.id.chatInfo)
        // TODO: 进入聊天室详情页面 activity
        // chatinfo.setOnClickListener {  }

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

    // FIXME: messageId -> chatId ?
    suspend fun getMessageInfo(messageId: String){
        // val contactDataSource = ContactDataSource()
        val messageRequest = MessageRequest()
        val result: Result<MessageInfo>

        withContext(Dispatchers.IO) {
            result = messageRequest.getMessage(messageId)
        }

        if (result is Result.Success) {
            _messageInfo.value = result.data
            // FIXME: 会有顺序不对的问题
            val message = Message(result.data.messageId, result.data.senderId, result.data.nickname, result.data.avatarIcon, result.data.recalled, result.data.content, result.data.time)
            messages.add(0, message)
        } else {
            // TODO：抛出并解析异常
        }
    }


}