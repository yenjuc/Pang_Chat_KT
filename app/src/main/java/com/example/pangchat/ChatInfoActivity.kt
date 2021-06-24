package com.example.pangchat


import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.ChatMemberAdapter
import com.example.pangchat.chat.data.ChatInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.chat.data.ChatUserInfo
import com.example.pangchat.fragment.data.FileDataSource
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.UploadResult
import com.example.pangchat.user.User
import com.example.pangchat.user.data.CommonResp
import com.example.pangchat.user.data.UserRequest
import com.example.pangchat.user.data.UserResult
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
import com.github.kittinunf.fuel.core.BlobDataPart
import com.github.kittinunf.fuel.core.DataPart
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList

class ChatInfoActivity : AppCompatActivity() {

    var chat: Chat? = null
    var chatId: String? = null
    var members = LinkedList<User?>()

    var chatName: TextView? = null
    var chatAvatar: ImageView ?= null
    var chatAvatarUrl: String ?= null
    var hasChange: Boolean = false

    var chatNameLayout: LinearLayout? = null
    var chatAvatarLayout: LinearLayout? = null
    var chatLeaveLayout: LinearLayout? = null
    var recyclerView : RecyclerView? = null

    var _uploadInfo = MutableLiveData<UploadResult>()

    // 拍照回传码
    val CAMERA_REQUEST_CODE = 0;
    // 相册选择回传码
    val GALLERY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_info)

        chatId = intent.getStringExtra("chatId")
        chatName = findViewById<TextView>(R.id.chatName)
        chatAvatar = findViewById(R.id.chatInfoChatAvatar)
        recyclerView = findViewById<RecyclerView>(R.id.chatInfoMembersView)
        recyclerView?.adapter = ChatMemberAdapter(this, members, webSocketClient.urlToBitmap)
        val gridLayoutManager = GridLayoutManager(this, 5)
        recyclerView?.layoutManager = gridLayoutManager

        chatNameLayout = findViewById(R.id.chatNameLayout)
        chatAvatarLayout = findViewById(R.id.chatAvatarLayout)
        chatLeaveLayout = findViewById(R.id.chatLeaveLayout)
    }

    fun updateChatInfo(){
        if (chatId != null) {
            lifecycleScope.launch {
                members.clear()
                getChatMember(chatId!!)
                runOnUiThread {
                    recyclerView?.adapter?.notifyDataSetChanged()
                }
                if(members != null){
                    for(member in members){
                        if(!webSocketClient.urlToBitmap.keys.contains(member?.getAvatar())){
                            downloadImage(member!!.getAvatar(), recyclerView!!)
                        }
                    }
                }
                recyclerView?.adapter?.notifyDataSetChanged()
                if(!hasChange){
                    if(!webSocketClient.urlToBitmap.containsKey(chat!!.getChatAvatar())){
                        // 发起下载图片请求
                        val bit: Bitmap;
                        withContext(Dispatchers.IO) {
                            val result = CookiedFuel.get(chat!!.getChatAvatar()).awaitByteArray();
                            bit = BitmapFactory.decodeByteArray(result, 0, result.size)
                            webSocketClient.urlToBitmap[chat!!.getChatAvatar()!!] = bit
                        }
                    }
                    chatAvatar!!.setImageBitmap(webSocketClient.urlToBitmap[chat!!.getChatAvatar()])
                }
                hasChange = false

                runOnUiThread{
                    if(chat != null){
                        chatName?.text = chat!!.getChatName()
                        members.add(User("-1", "-1", "-1", "-1"))
                        recyclerView?.adapter?.notifyDataSetChanged()
                        if(!chat!!.getIsGroup()){
                            chatNameLayout?.visibility = View.GONE
                            chatAvatarLayout?.visibility = View.GONE
                            chatLeaveLayout?.visibility = View.GONE
                        }
                    }
                }
            }
        }

        val back = findViewById<ImageView>(R.id.chatInfoBackward)
        back.setOnClickListener {
            finish()
        }


        chatLeaveLayout?.setOnClickListener {
            lifecycleScope.launch {
                if(chatId != null && leaveChat(chatId!!)){
                    activityFinish()
                }
            }
        }


        chatNameLayout?.setOnClickListener {
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

        chatAvatarLayout?.setOnClickListener {
            val pickIntent : Intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }

    }

    override fun onResume() {
        super.onResume()
        webSocketClient.context = this
        updateChatInfo()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                print("qwq")
            }
            else if (requestCode == GALLERY_REQUEST_CODE){
                try {
                    //该uri是上一个Activity返回的
                    val imageUri = data?.getData();
                    if(imageUri!=null) {
                        var inputImage: InputStream
                        hasChange = true
                        chatAvatar?.setImageBitmap(BitmapFactory.decodeStream(contentResolver?.openInputStream(imageUri)))

                        // 向服务器发送请求
                        MainScope().launch {
                            val splited = imageUri.lastPathSegment!!.split("/");
                            withContext(Dispatchers.IO) {
                                inputImage =
                                    contentResolver?.openInputStream(imageUri)!!
                            }
                            uploadImage(
                                BlobDataPart(
                                    inputImage,
                                    "file",
                                    splited[splited.size - 1]
                                )
                            )
                            modifyChatAvatar(chatId!!, _uploadInfo.value!!.url)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }

            }else if(requestCode == 100){
                val reply = data?.getStringExtra("chatName")
                chatName?.text = reply
            }
        }
    }

    suspend fun uploadImage(file: DataPart) {
        val fileDataSource = FileDataSource()

        val result: Result<UploadResult>

        withContext(Dispatchers.IO) {
            result = fileDataSource.uploadFile(file)
        }

        if (result is Result.Success) {
            _uploadInfo.value = result.data
        } else {
            // TODO：抛出并解析异常
        }
    }

    private suspend fun modifyChatAvatar(chatId: String, value: String): Boolean{
        val chatRequest = ChatRequest()
        val result: ChatResult<ChatInfo>

        withContext(Dispatchers.IO) {
            result = chatRequest.chatModify(chatId, "chatAvatar",value)
        }

        if (result is ChatResult.Success) {
            Log.d("chat", "success")
        } else {
            // TODO：抛出并解析异常
        }

        return result is ChatResult.Success
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

    public fun downloadImage(url: String, recyclerView: RecyclerView){
        lifecycleScope.launch {
            downloadBitmap(url)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    suspend fun downloadBitmap(url: String){
        withContext(Dispatchers.IO){
            val result = CookiedFuel.get(url).awaitByteArray();
            if(result != null){
                val bit: Bitmap = BitmapFactory.decodeByteArray(result, 0, result.size)
                webSocketClient.urlToBitmap!!.put(url, bit)
            }
        }
    }

}