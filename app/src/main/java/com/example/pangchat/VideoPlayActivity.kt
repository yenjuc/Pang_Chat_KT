package com.example.pangchat


import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.chat.data.ChatInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class VideoPlayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)
        var videoView: VideoView = findViewById(R.id.videoView)

        var url: String? = intent.getStringExtra("videoUrl")

        var mediaController: MediaController = MediaController(this)

        /*
        if(url != null) {
            lifecycleScope.launch {
                // 发起下载视频请求
                val byteArray = ByteArray
                withContext(Dispatchers.IO) {
                    val result = CookiedFuel.get(url).awaitByteArray();
                    bit = BitmapFactory.decodeByteArray(result, 0, result.size)
                }

            }
        }

         */


        if(url != null){
            videoView.setVideoURI(Uri.parse("http://183.172.182.59:7000/test/1624397853917_4864.mp4"))
            // videoView.set
        }
        mediaController.setMediaPlayer(videoView)
        videoView.setMediaController(mediaController)
        videoView.start()
        /*
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
        */
    }

    /*
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

     */


}