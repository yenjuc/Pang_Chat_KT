package com.example.pangchat


import android.os.Build
import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

class VideoPlayActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_play)

        webSocketClient.context = this

        var videoView: VideoView = findViewById(R.id.videoView)

        var url: String? = intent.getStringExtra("videoUrl")

        var mediaController: MediaController = MediaController(this)

        var filename: String? = url?.substring(url.lastIndexOf('/'))
        if (url != null) {
            lifecycleScope.launch {
                // 发起下载视频请求
                withContext(Dispatchers.IO) {
                    val result = CookiedFuel.get(url).awaitByteArray();
                    val inStream: InputStream = ByteArrayInputStream(result)
                    val file: File = File(filesDir, filename)
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    val outStream: FileOutputStream = FileOutputStream(file)
                    val b: ByteArray = ByteArray(1024)
                    var len = 0
                    len = inStream.read(b)
                    while (len != -1) {
                        outStream.write(b, 0, len)
                        len = inStream.read(b)
                    }
                    inStream.close()
                    outStream.close()
                    runOnUiThread {
                        videoView.setVideoPath(filesDir.toString() + filename)
                        mediaController.setMediaPlayer(videoView)
                        videoView.setMediaController(mediaController)
                        videoView.start()
                    }
                }
            }
        }
    }
}