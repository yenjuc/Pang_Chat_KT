package com.example.pangchat


import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ChatInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_info)

        bindClick()
    }

    fun bindClick(){
        val back = findViewById<ImageView>(R.id.chatInfoBackward)
        back.setOnClickListener { this.finish() }

        val chatName = findViewById<TextView>(R.id.chatName)
        // TODO: 设置聊天室名字
        chatName.text = "学习的胖"

        // TODO: set 各种头像、置顶、静音 blablabla

    }

}