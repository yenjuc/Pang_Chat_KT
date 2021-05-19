package com.example.pangchat.chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.pangchat.R
import java.util.*

class ChatAdapter(private val data: LinkedList<Chat?>?, private val context: Context?) : BaseAdapter() {
    override fun getCount(): Int {
        if (data != null) {
            return data.size
        }
        return 0
    }

    override fun getItem(position: Int): Any? {
        if (data != null) {
            return data[position]
        }
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.item_list_chat, parent, false)
        val chat = data?.get(position)
        // 修改View中各个控件的属性，使之显示对应位置Chat的内容
        // 使用convertView.findViewById()方法来寻找对应的控件
        // 控件ID见 res/layout/item_list_chat.xml
        // TODO
        val avatar = convertView.findViewById<ImageView?>(R.id.avatar_icon)
        if(chat != null){
            avatar?.setImageResource(chat.getAvatarIcon())
            val nickname = convertView.findViewById<TextView?>(R.id.nickname_text)
            nickname?.text = chat.getNickname()
            val lastTime = convertView.findViewById<TextView?>(R.id.last_speak_time_text)
            lastTime?.text = chat.getLastSpeakTime()
            val lastConx = convertView.findViewById<TextView?>(R.id.last_speak_text)
            lastConx?.text = chat.getLastSpeak()
            return convertView
        }
        return null
    }

}