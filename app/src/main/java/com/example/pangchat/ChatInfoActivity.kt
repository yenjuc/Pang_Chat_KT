package com.example.pangchat


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.ChatMemberAdapter
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.chat.data.ChatUserInfo
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

    var chatName: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_info)

        var chatId: String? = intent.getStringExtra("chatId")


        chatName = findViewById<TextView>(R.id.chatName)
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
                        chatName?.text = chat!!.getChatName()
                        members.add(User("-1", "-1", "-1", "-1"))
                        recyclerView.adapter?.notifyDataSetChanged()
                        if(!chat!!.getIsGroup()){
                            val chatNameLayout: LinearLayout = findViewById(R.id.chatNameLayout)
                            chatNameLayout.visibility = View.GONE
                            val chatAvatarLayout: LinearLayout = findViewById(R.id.chatAvatarLayout)
                            chatAvatarLayout.visibility = View.GONE
                            val chatLeaveLayout: LinearLayout = findViewById(R.id.chatLeaveLayout)
                            chatLeaveLayout.visibility = View.GONE
                        }
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

        val chatName = findViewById<LinearLayout>(R.id.chatNameLayout)
        chatName.setOnClickListener {
            if(chatId != null){
                val intent = Intent(this, ChatnameModifyActivity::class.java)
                intent.putExtra("chatId", chatId)
                intent.putExtra("chatName", chat?.getChatName())
                try {
                    this.startActivity(intent)
                } catch (ActivityNotFoundException: Exception) {
                    Log.d("ImplicitIntents", "Can't handle this!")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val chatname = intent.getStringExtra("chatName")
        if(chatname != null){
            chatName?.text = chatname
        }
    }

    private suspend fun activityFinish(){
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