package com.example.pangchat


import android.app.Activity
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationManager
import android.media.MediaRecorder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.pangchat.chat.data.ChatMessageInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.fragment.data.FileDataSource
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.UploadResult
import com.example.pangchat.message.Message
import com.example.pangchat.message.MessageAdapter
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

    private var chat: Chat? = null

    private var data: LinkedList<Message?>? = null

    private var messages: ArrayList<Message>? = null

    private var urlToBitmap: MutableMap<String, Bitmap> = mutableMapOf()

    private var mediaType : String = "image"

    private var chatId: String? = null

    private var recyclerView: RecyclerView? = null

    private var _uploadInfo = MutableLiveData<UploadResult>()


    // 相册选择回传码
    val GALLERY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        chatId = intent.getStringExtra("chatId")
        recyclerView = findViewById(R.id.chatRecyclerView)
        data = LinkedList()
        messages = ArrayList()
        recyclerView?.adapter = MessageAdapter(webSocketClient.userId!!, this, data, urlToBitmap)
        // 取得对应聊天的内容
        lifecycleScope.launch {
            if (chatId != null) {
                getChatAndMessage(chatId!!)
                if(messages != null){
                    for(message in messages!!){
                        data!!.add(message)
                        if(!urlToBitmap.keys.contains(message.getAvatar())){
                            downloadBitmap(message.getAvatar())
                        }
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
        }

        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager

        // init()
        val back = findViewById<ImageView>(R.id.chatBackward)
        back.setOnClickListener { this.finish() }

        val chatinfo = findViewById<ImageView>(R.id.chatInfo)
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


        val videoSender = findViewById<ImageView>(R.id.chatVideo)
        videoSender.setOnClickListener{
            // 修改头像
            val pickIntent : Intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*")
            mediaType = "video"
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }


        val imageSender = findViewById<ImageView>(R.id.chatImage)
        imageSender.setOnClickListener{
            val pickIntent : Intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            mediaType = "image"
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
        }


        val chatAction = findViewById<ImageView>(R.id.chatAction)
        chatAction.setOnClickListener {
            if(chatInput.text?.isEmpty() == false){
                Log.d("Debug", "not empty")
                lifecycleScope.launch {
                    if (chatId != null) {
                        sendMessage(chatInput.text.toString(), "text")
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
                Toast.makeText(this, "发送当前位置", Toast.LENGTH_LONG).show()
                lifecycleScope.launch {
                    sendMessage(location.latitude.toString() + ";" + location.longitude.toString(), "location")
                }
            }else{
                Toast.makeText(this, "获取当前地理位置失败！请打开GPS或网络后再试一次。", Toast.LENGTH_LONG).show()
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


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == GALLERY_REQUEST_CODE){
                try {
                    //该uri是上一个Activity返回的
                    val uri = data?.data;
                    if(uri!=null) {
                        // 向服务器发送请求
                        MainScope().launch {
                            val splited = uri.lastPathSegment!!.split("/");
                            val input: InputStream
                            withContext(Dispatchers.IO) {
                                input = getContentResolver()?.openInputStream(uri)!!
                            }

                            uploadFile(
                                BlobDataPart(
                                    input,
                                    "file",
                                    splited[splited.size - 1]
                                ), uri
                            )
                            // if()

                            /*
                            withContext(Dispatchers.IO) {
                                val result = CookiedFuel.get(_uploadInfo?.value!!.url).awaitByteArray()
                                if(mediaType.compareTo("image") == 0){
                                    var bit: Bitmap = BitmapFactory.decodeByteArray(result, 0, result.size)

                                }
                                // MediaRecorder.VideoEncoder
                                // bit = BitmapFactory.decodeByteArray(result, 0, result.size)
                            }

                             */
                            // imageView?.setImageBitmap(bit) // 必须放在IO外面
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

    suspend fun uploadFile(file: DataPart, uri: Uri) {
        val fileDataSource = FileDataSource()

        val result: Result<UploadResult>

        withContext(Dispatchers.IO) {
            result = fileDataSource.uploadFile(file)
        }

        if (result is Result.Success) {
            _uploadInfo.value = result.data
            if(mediaType.compareTo("image") == 0){
                var bit: Bitmap = BitmapFactory.decodeStream(contentResolver?.openInputStream(uri))
                urlToBitmap!![_uploadInfo?.value!!.url] = bit
                sendMessage(_uploadInfo?.value!!.url, "image")
                recyclerView?.adapter?.notifyDataSetChanged()
                // downLoadImageBitmap(_uploadInfo?.value!!.url)
            }
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

    private suspend fun sendMessage(content: String, type: String){
        val messageRequest = MessageRequest()
        val result: MessageResult<MessageResp>

        withContext(Dispatchers.IO) {
            result = messageRequest.sendMessage(chatId!!, webSocketClient.userId!!, type, content)
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

    fun deleteMessage(index: Int, messageId: String, userId: String){
        lifecycleScope.launch {
            if(deleteMessage(messageId, userId)){
                data?.get(index)?.addBlocked(userId)
                recyclerView?.adapter?.notifyDataSetChanged()
            }
        }
    }

    private suspend fun deleteMessage(messageId: String, userId: String): Boolean{
        val messageRequest = MessageRequest()
        val result: MessageResult<MessageResp>

        withContext(Dispatchers.IO) {
            result = messageRequest.deleteMessage(messageId, userId)
        }

        return result is MessageResult.Success
    }

    public fun downLoadImageBitmap(url: String){
        lifecycleScope.launch {
            downloadBitmap(url)
            recyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    suspend fun downloadBitmap(url: String){
        withContext(Dispatchers.IO){
            val result = CookiedFuel.get(url).awaitByteArray();
            val bit: Bitmap = BitmapFactory.decodeByteArray(result, 0, result.size)
            urlToBitmap!!.put(url, bit)
        }
    }


}