package com.example.pangchat.chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R
import com.example.pangchat.chat.ChatAdapter.ChatViewHolder
import java.util.*

class ChatAdapter(private val data: LinkedList<Chat?>?) : RecyclerView.Adapter<ChatViewHolder?>() {

    class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = itemView.findViewById<ImageView?>(R.id.avatar_icon)
        var nickname: TextView? = itemView.findViewById<TextView?>(R.id.nickname_text)
        val lastTime: TextView? = itemView.findViewById<TextView?>(R.id.last_speak_time_text)
        val lastConx: TextView? = itemView.findViewById<TextView?>(R.id.last_speak_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        // TODO
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        // TODO
        val chat = data?.get(position)
        if (chat != null) {
            holder.avatar?.setImageResource(chat.getAvatarIcon())
            holder.nickname?.text = chat.getNickname()
            holder.lastTime?.text = chat.getLastSpeakTime()
            holder.lastConx?.text = chat.getLastSpeak()
        }
    }

    override fun getItemCount(): Int {
        if (data != null) {
            return data.size
        }
        return 0
    }

}