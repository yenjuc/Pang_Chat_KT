package com.example.pangchat

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.ArrayRes
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.discover.data.DiscoverRequest
import com.example.pangchat.discover.data.DiscoverResult
import com.example.pangchat.discover.data.sendPostResult
import com.example.pangchat.fragment.data.FileDataSource
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.UploadResult
import com.example.pangchat.message.data.MessageRequest
import com.example.pangchat.message.data.MessageResp
import com.example.pangchat.message.data.MessageResult
import com.example.pangchat.websocketClient.webSocketClient
import com.github.kittinunf.fuel.core.BlobDataPart
import com.github.kittinunf.fuel.core.DataPart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.util.*
import kotlin.collections.ArrayList


class newPostActivity  : AppCompatActivity() {
    private var userId: String? = null
    private var content:String? = null
    private var images:ArrayList<String>? = null
    private var videos:ArrayList<String>? =null
    private var imageViewList:ArrayList<ImageView>?= null
    private var deleteViewList:ArrayList<ImageView>?=null
    private var text:EditText?=null
    private var mediaType : String = "image"
    private var _uploadInfo = MutableLiveData<UploadResult>()
    private var urlToBitmap: MutableMap<String, Bitmap> = mutableMapOf()
    private var upload:Boolean = false
    private var imageCurrent:Int? = null
    private var uriList:ArrayList<Uri>?= ArrayList()

    // 相册选择回传码
    val GALLERY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        userId = intent.getStringExtra("userId")

        val back = findViewById<ImageView>(R.id.goback)
        var video_button = findViewById<ImageView>(R.id.add_video)
        var post = findViewById<Button>(R.id.button_post)
        text = findViewById<EditText>(R.id.post_text)

        content = ""

        imageViewList = ArrayList()
        imageViewList!!.add(findViewById(R.id.add_image_0))
        imageViewList!!.add(findViewById(R.id.add_image_1))
        imageViewList!!.add(findViewById(R.id.add_image_2))
        imageViewList!!.add(findViewById(R.id.add_image_3))

        deleteViewList = ArrayList()
        deleteViewList!!.add(findViewById(R.id.delete_image_0))
        deleteViewList!!.add(findViewById(R.id.delete_image_1))
        deleteViewList!!.add(findViewById(R.id.delete_image_2))
        deleteViewList!!.add(findViewById(R.id.delete_image_3))


        images = ArrayList()

//        text?.onFocusChangeListener = View.OnFocusChangeListener() { view: View, b: Boolean ->
//            //当EditText失去焦点时，隐藏软键盘
//                if (!b) {
//                    closeKeyBoard()
//                }
//
//        };

        for(i in 1..3){
            imageViewList!![i].visibility=View.GONE
        }

        for(x in deleteViewList!!){
            x.visibility=View.GONE
        }

        back.setOnClickListener(View.OnClickListener {
            val back_intent = Intent(this, MainActivity::class.java)
            back_intent.putExtra("fragment","discover")
            try {
                startActivity(back_intent)
                finish()
            } catch (e: Exception) {
                Log.d("ImplicitIntents", "Can't handle this!")
            }
        })

        post.setBackgroundColor(Color.parseColor("#EEEEEE"))
        post.isEnabled=false
        post.setOnClickListener {
            MainScope().launch {
                if(!uriList.isNullOrEmpty()){
                    for(uri in uriList!!){
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
                    }
                }


                    content?.let { it1 -> sendPost(images!!, it1,mediaType)}


//            lifecycleScope.launch {
//                 }

            }
            val post_intent = Intent(this, MainActivity::class.java)
            post_intent.putExtra("fragment","discover")
            try {
                startActivity(post_intent)
                finish()
            } catch (e: Exception) {
                Log.d("ImplicitIntents", "Can't handle this!")
            }
        }

        (text as EditText?)?.afterTextChanged {
            content = it
            if(content!=""){
                post.background= getDrawable(R.drawable.btstyle)
                post.isEnabled=true
            } else if(uriList!!.isEmpty()){
                post.isEnabled = false
                post.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }


        video_button.setOnClickListener {
            for (x in imageViewList!!) {
                x.visibility = View.GONE
            }
            for(x in deleteViewList!!){
                x.visibility = View.GONE
            }
            val videoSender = findViewById<ImageView>(R.id.add_video)
            videoSender.setOnClickListener {
                val pickIntent: Intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                );
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "video/*")
                mediaType = "video"
                startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);
            }
        }

        for(i in 0..3){
            imageViewList!![i].setOnClickListener {
                //TODO 上传图片
                //假设上传成功
                //先随意设置一张图片
                imageCurrent = i
                val pickIntent : Intent = Intent(Intent.ACTION_PICK,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                mediaType = "image"
                startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);



                closeKeyBoard()
//                imageViewList!![i].setImageResource(R.drawable.image1)
//                images!!.add("1")
                deleteViewList!![i].visibility=View.VISIBLE
                imageViewList!![i].isEnabled=false
                if (i < 3) {
                    imageViewList!![i + 1].visibility = View.VISIBLE
                }
                if (!uriList?.isEmpty()!!) {
                    post.background = getDrawable(R.drawable.btstyle)
                    post.isEnabled = true
                }
            }
        }

        for(i in 0..3){
            deleteViewList!![i].setOnClickListener {
                closeKeyBoard()
//                images!!.removeAt(i)
                uriList?.removeAt(i)


                for (j in 0..(uriList!!.size - 1)) {
//                    imageViewList!![j].setImageResource(R.drawable.image1)
                    imageViewList!![j].setImageBitmap(BitmapFactory.decodeStream(uriList?.get(j)?.let { it1 ->
                        this?.getContentResolver()?.openInputStream(
                            it1
                        )
                    }))
                }
                imageViewList!![uriList!!.size].setImageResource(R.drawable.ic_add1)
                deleteViewList!![uriList!!.size].visibility = View.GONE
                imageViewList!![uriList!!.size].isEnabled = true
                for (j in (uriList!!.size + 1)..3) {
                    imageViewList!![j].visibility = View.GONE
                    deleteViewList!![j].visibility = View.GONE
                }

                if (uriList!!.isEmpty() && content == "") {
                    post.setBackgroundColor(Color.parseColor("#EEEEEE"))
                    post.isEnabled = false
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        webSocketClient.context = this
    }

    fun closeKeyBoard() {
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            text?.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private suspend fun sendPost(images:ArrayList<String>,content: String,type:String){
        val discoverRequest = DiscoverRequest()
        val result: DiscoverResult<sendPostResult>

        withContext(Dispatchers.IO) {
            // FIXME: type
            result = discoverRequest.sendPost(webSocketClient.userId!!, images, content,type)
        }

        if (result is DiscoverResult.Success) {
            Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("ERROR",result.toString())
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
                        if(mediaType=="image") {
                            imageCurrent?.let {
                                imageViewList?.get(it)?.setImageBitmap(
                                    BitmapFactory.decodeStream(
                                        this?.getContentResolver()?.openInputStream(uri)
                                    )
                                )
                            }
                        }
                        uriList?.add(uri)
                        Toast.makeText(this, "上传成功", Toast.LENGTH_SHORT).show()
                        var post = findViewById<Button>(R.id.button_post)
                        if (post != null) {
                            post.background = getDrawable(R.drawable.btstyle)
                        }
                        if (post != null) {
                            post.isEnabled = true
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace();
                }

            }
        }
    }


    suspend fun uploadFile(file: DataPart, uri: Uri) {
        val fileDataSource = FileDataSource()

        val result: Result<UploadResult>

        withContext(Dispatchers.IO) {
            result = fileDataSource.uploadFile(file)
        }

        if (result is Result.Success) {
            _uploadInfo.value = result.data
//                var bit: Bitmap = BitmapFactory.decodeStream(contentResolver?.openInputStream(uri))
//                urlToBitmap!![_uploadInfo?.value!!.url] = bit
                images?.add(_uploadInfo?.value!!.url)
        } else {
            // TODO：抛出并解析异常
        }
    }

}