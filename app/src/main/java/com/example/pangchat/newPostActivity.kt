package com.example.pangchat

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.discover.data.DiscoverRequest
import com.example.pangchat.discover.data.DiscoverResult
import com.example.pangchat.discover.data.sendPostResult
import com.example.pangchat.websocketClient.webSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

//FIXME:纯图片存在问题
class newPostActivity  : AppCompatActivity() {
    private var userId: String? = null
    private var content:String? = null
    private var images:ArrayList<String>? = null
    private var imageViewList:ArrayList<ImageView>?= null
    private var deleteViewList:ArrayList<ImageView>?=null
    private var text:EditText?=null

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)

        webSocketClient.context = this

        userId = intent.getStringExtra("userId")

        val back = findViewById<ImageView>(R.id.goback)
        val post = findViewById<Button>(R.id.button_post)
        text = findViewById<EditText>(R.id.post_text)

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
            lifecycleScope.launch {
                content?.let { it1 -> sendPost(images!!, it1) }
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
            } else if(images!!.isEmpty()){
                post.isEnabled = false
                post.setBackgroundColor(Color.parseColor("#EEEEEE"))
            }
        }



        for(i in 0..3){
            imageViewList!![i].setOnClickListener {
                //TODO 上传图片
                //假设上传成功
                //先随意设置一张图片
                closeKeyBoard()
                imageViewList!![i].setImageResource(R.drawable.image1)
                images!!.add("1")
                deleteViewList!![i].visibility=View.VISIBLE
                imageViewList!![i].isEnabled=false
                if (i < 3) {
                    imageViewList!![i + 1].visibility = View.VISIBLE
                }
                if (!images?.isEmpty()!!) {
                    post.background = getDrawable(R.drawable.btstyle)
                    post.isEnabled = true
                }
            }
        }

        for(i in 0..3){
            deleteViewList!![i].setOnClickListener {
                closeKeyBoard()
                images!!.removeAt(i)


                for (j in 0..(images!!.size - 1)) {
                    imageViewList!![j].setImageResource(R.drawable.image1)
                }
                imageViewList!![images!!.size].setImageResource(R.drawable.ic_add1)
                deleteViewList!![images!!.size].visibility = View.GONE
                imageViewList!![images!!.size].isEnabled = true
                for (j in (images!!.size + 1)..3) {
                    imageViewList!![j].visibility = View.GONE
                    deleteViewList!![j].visibility = View.GONE
                }

                if (images!!.isEmpty() && content == "") {
                    post.setBackgroundColor(Color.parseColor("#EEEEEE"))
                    post.isEnabled = false
                }
            }
        }
    }

    fun closeKeyBoard() {
        val inputMethodManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(
            text?.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS
        )
    }

    private suspend fun sendPost(images:ArrayList<String>,content: String){
        val discoverRequest = DiscoverRequest()
        val result: DiscoverResult<sendPostResult>

        withContext(Dispatchers.IO) {
            // FIXME: type
            result = discoverRequest.sendPost(webSocketClient.userId!!, images, content)
        }

        if (result is DiscoverResult.Success) {
            Toast.makeText(this, "发送成功", Toast.LENGTH_SHORT).show()
        } else {
            Log.d("ERROR",result.toString())
        }
    }


}