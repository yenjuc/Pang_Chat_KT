package com.example.pangchat


import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.data.ChatInfo
import com.example.pangchat.chat.data.ChatMessageInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.message.Message
import com.example.pangchat.message.MessageAdapter
import com.example.pangchat.message.data.*
import com.example.pangchat.websocketClient.webSocketClient
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private var chat: Chat? = null

    private var data: LinkedList<Message?>? = null

    private var messages: ArrayList<Message>? = null

    private var chatId: String? = null

    private var recyclerView: RecyclerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        // TODO: 看看是否可拉取完再载入

        chatId = intent.getStringExtra("chatId")
        recyclerView = findViewById(R.id.chatRecyclerView)
        data = LinkedList()
        messages = ArrayList()
        recyclerView?.adapter = MessageAdapter(webSocketClient.userId!!, this, data)
        // 取得对应聊天的内容
        lifecycleScope.launch {
            if (chatId != null) {
                getChatAndMessage(chatId!!)
                for(message in messages!!){
                    data!!.add(message)
                }
                recyclerView?.adapter?.notifyDataSetChanged()
                recyclerView?.scrollToPosition(messages!!.size - 1)
                runOnUiThread {
                    val chatname = findViewById<TextView>(R.id.chatName)
                    chatname.text = chat?.getChatName()
                    // FIXME: 两人聊天应改成对方名称
                    /*
                    if(chat?.getIsGroup() == false){
                        chatname.text = chat?.getChatName()
                    }else{

                        chatname.text = "对方用户名"
                    }*/
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
            intent.putExtra("chatId", chatId)
            try {
                startActivity(intent)
                this.finish()
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
                        recyclerView?.scrollToPosition(data!!.size - 1)
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

        val chatLocation = findViewById<ImageView>(R.id.chatLocation)
        chatLocation.setOnClickListener {

            val location = getLocation()
            if(location != null){
                val mapIntent: Intent = Uri.parse(
                    "geo:" + location.latitude + ", " + location.longitude
                ).let{
                        location ->
                    Intent(Intent.ACTION_VIEW, location)
                }

                val intentChooser = Intent.createChooser(mapIntent, "选择地图")

                try{
                    startActivity(intentChooser)
                }catch (ActivityNotFoundException: Exception){
                    Log.d("ImplicitIntents", "Can't handle this!")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d("back: ", "resume")
    }

    private fun getLocation(): Location?{
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION), 1)
            return null
        }

        var location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (location == null) {
                Toast.makeText(this, "空位置", Toast.LENGTH_LONG).show()
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location == null) {
                    Toast.makeText(this, "网络位置为空", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this, "使用网络位置", Toast.LENGTH_LONG).show()
                }
            }
        // }
        return location
    }


    @Override
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 1){

            for(index in permissions.indices){
                if (grantResults[index] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "" + "权限" + permissions[index] + "申请成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[index] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    public fun setInput(text: String){
        val chatInput = findViewById<TextInputEditText>(R.id.chatInput)
        chatInput.setText(text)
    }

    private suspend fun getChatAndMessage(chatId: String){
        val chatRequest = ChatRequest()
        val result: ChatResult<ChatMessageInfo>

        withContext(Dispatchers.IO){
            result = chatRequest.getMessagesOfChat(chatId)
        }

        if (result is ChatResult.Success) {
            chat = result.data.chat
            messages = result.data.records
        } else {
            Log.d("error: ", "get chat info error")
            // TODO：抛出并解析异常
        }
    }

    private suspend fun sendMessage(content: String){
        val messageRequest = MessageRequest()
        val result: MessageResult<MessageResp>

        withContext(Dispatchers.IO) {
            // FIXME: type
            result = messageRequest.sendMessage(chatId!!, webSocketClient.userId!!, "text", content)
        }

        if (result is MessageResult.Success) {
            data?.add(result.data.message)
        } else {
            // TODO：抛出并解析异常
        }
    }

    fun recallMessage(index: Int, messageId: String){
        lifecycleScope.launch {
            if(recallMessage(messageId)){
                data?.get(index)?.setRecalled()
                recyclerView?.adapter?.notifyDataSetChanged()
            }
        }
    }

    private suspend fun recallMessage(messageId: String) : Boolean{
        val messageRequest = MessageRequest()
        val result: MessageResult<MessageResp>

        withContext(Dispatchers.IO) {
            result = messageRequest.recallMessage(messageId)
        }

        return result is MessageResult.Success
    }

}