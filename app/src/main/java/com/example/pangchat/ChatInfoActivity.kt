package com.example.pangchat


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.ChatMemberAdapter
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.chat.data.ChatUserInfo
import com.example.pangchat.message.data.MessageRequest
import com.example.pangchat.message.data.MessageResp
import com.example.pangchat.message.data.MessageResult
import com.example.pangchat.user.User
import com.example.pangchat.user.data.CommonResp
import com.example.pangchat.user.data.UserRequest
import com.example.pangchat.user.data.UserResult
import com.example.pangchat.websocketClient.webSocketClient

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ChatInfoActivity : AppCompatActivity() {

    var chat: Chat? = null
    var members = LinkedList<User?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_info)

        var chatId: String? = intent.getStringExtra("chatId")


        val chatName = findViewById<TextView>(R.id.chatName)
        val recyclerView = findViewById<RecyclerView>(R.id.chatInfoMembersView)
        recyclerView.adapter = ChatMemberAdapter(this, members)
        val gridLayoutManager = GridLayoutManager(this, 5)
        recyclerView.layoutManager = gridLayoutManager

        if (chatId != null) {
            Log.d("chatId", chatId)
            lifecycleScope.launch {
                getChatMember(chatId)
                runOnUiThread{
                    if(chat != null){
                        chatName.text = chat!!.getChatName()
                        members.add(User("-1", "-1", "-1"))
                        recyclerView.adapter?.notifyDataSetChanged()
                    }
                }
            }
        }

        val back = findViewById<ImageView>(R.id.chatInfoBackward)
        back.setOnClickListener {
            if(chatId != null){
                Log.d("click chatid: ", chatId)
                val intent = Intent(this, ChatActivity::class.java)
                intent.putExtra("chatId", chatId)
                try {
                    this.startActivity(intent)
                    this.finish()
                } catch (ActivityNotFoundException: Exception) {
                    Log.d("ImplicitIntents", "Can't handle this!")
                }
            }
        }

        val leave = findViewById<LinearLayout>(R.id.chatLeaveLayout)
        var success: Boolean ? = null
        leave.setOnClickListener {
            lifecycleScope.launch {
                if(chatId != null && leaveChat(chatId)){
                    activityFinish()
                }
            }
        }
    }

    private suspend fun activityFinish(){
        /*
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("fragment", "chats")
        try {
            startActivity(intent)
        } catch (ActivityNotFoundException: Exception) {
            Log.d("ImplicitIntents", "Can't handle this!")
        }

         */
        this.finish()
    }

    private suspend fun getChatMember(chatId: String){
        val chatRequest = ChatRequest()
        val result: ChatResult<ChatUserInfo>

        withContext(Dispatchers.IO) {
            result = chatRequest.getChatAndMembers(chatId)
        }

        if (result is ChatResult.Success) {
            chat = result.data.chat
            for(user in result.data.members){
                members.add(user)
            }
        } else {
            // TODO：抛出并解析异常
        }
    }

    private suspend fun leaveChat(chatId: String): Boolean{
        val userRequest = UserRequest()
        val result: UserResult<CommonResp>

        withContext(Dispatchers.IO) {
            var userId = ArrayList<String>()
            userId.add(webSocketClient.userId!!)
            result = userRequest.userChat(userId, chatId, "leave")
        }

        if (result is UserResult.Success) {
            Log.d("chat", "success")
        } else {
            // TODO：抛出并解析异常
        }

        return result is UserResult.Success
    }

    fun toAddChatMember(){
        val intent = Intent(this, SelectFriendsActivity::class.java)
        intent.putExtra("chatId", chat?.getId())
        intent.putStringArrayListExtra("members", chat?.getMembers())
        try {
            this.startActivity(intent)
            this.finish()
        } catch (ActivityNotFoundException: Exception) {
            Log.d("ImplicitIntents", "Can't handle this!")
        }
    }

}