package com.example.pangchat


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.data.ChatMessageInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.fragment.data.FileDataSource
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.UploadResult
import com.example.pangchat.message.Message
import com.example.pangchat.message.MessageAdapter
import com.example.pangchat.message.data.MessageInfo
import com.example.pangchat.message.data.MessageRequest
import com.example.pangchat.message.data.MessageResp
import com.example.pangchat.message.data.MessageResult
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
import com.github.kittinunf.fuel.core.BlobDataPart
import com.github.kittinunf.fuel.core.DataPart
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class ChatActivity : AppCompatActivity() {

    private val _messageInfo = MutableLiveData<MessageInfo>()

    //private var chatInfo: ChatInfo? = null

    private var chat: Chat? = null

    private var data: LinkedList<Message?>? = null

    private var messages: ArrayList<Message>? = null

    private var chatId: String? = null

    private var recyclerView: RecyclerView? = null

    private var _uploadInfo: MutableLiveData<UploadResult> ?= null

    // 相册选择回传码
    val GALLERY_REQUEST_CODE = 1

    //private lateinit var messages:LinkedList<Message?>

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

        // 假设第一个是发送视频


        val videoSender = findViewById<ImageView>(R.id.imageView4)
        videoSender.setOnClickListener{
            // 修改头像
            val pickIntent : Intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }


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
    }

    /*
    override fun onResume() {
        super.onResume()
        if(chat != null && !chat!!.getMembers().contains(webSocketClient.userId)){
            this.finish()
        }
    }

     */

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE){
                try {
                    //该uri是上一个Activity返回的
                    val videoUri = data?.getData();
                    if(videoUri!=null) {

                        // 向服务器发送请求
                        MainScope().launch {
                            val splited = videoUri.lastPathSegment!!.split("/");
                            val inputVideo: InputStream
                            withContext(Dispatchers.IO) {
                                inputVideo = getContentResolver()?.openInputStream(videoUri)!!
                            }

                            uploadVideo(
                                BlobDataPart(
                                    inputVideo,
                                    "file",
                                    splited[splited.size - 1]
                                )
                            )

                            withContext(Dispatchers.IO) {
                                val result = CookiedFuel.get(_uploadInfo?.value!!.url).awaitByteArray();
                                // bit = BitmapFactory.decodeByteArray(result, 0, result.size)
                            }
                            // imageView?.setImageBitmap(bit) // 必须放在IO外面


//                            modifyAvatar(_uploadInfo.url)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }

            }
        }
    }


    public fun setInput(text: String){
        val chatInput = findViewById<TextInputEditText>(R.id.chatInput)
        chatInput.setText(text)
    }

    suspend fun uploadVideo(file: DataPart) {
        val fileDataSource = FileDataSource()

        val result: Result<UploadResult>

        withContext(Dispatchers.IO) {
            result = fileDataSource.uploadFile(file)
        }

        if (result is Result.Success) {
            // _uploadInfo = result.data
        } else {
            // TODO：抛出并解析异常
        }
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