package com.example.pangchat.chat

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.util.Log.DEBUG
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.ChatActivity
import com.example.pangchat.ChatInfoActivity
import com.example.pangchat.R
import com.example.pangchat.chat.ChatAdapter.ChatViewHolder
import java.util.*

class ChatAdapter(private val mContext: FragmentActivity?, private val data: LinkedList<Chat?>?, private val urlToBitmap: MutableMap<String, Bitmap>) : RecyclerView.Adapter<ChatViewHolder?>() {

    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = itemView.findViewById<ImageView?>(R.id.avatar_icon)
        var chatname: TextView? = itemView.findViewById<TextView?>(R.id.nickname_text)
        val lastTime: TextView? = itemView.findViewById<TextView?>(R.id.last_speak_time_text)
        val lastConx: TextView? = itemView.findViewById<TextView?>(R.id.last_speak_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_chat, parent, false)
        return ChatViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        val chat = data?.get(position)
        if (chat != null) {
            if(urlToBitmap.keys.contains(chat.getChatAvatar())){
                holder.avatar?.setImageBitmap(urlToBitmap[chat.getChatAvatar()])
            }
            holder.chatname?.text = chat.getChatName()
            holder.lastTime?.text = chat.getLastUpdateTime()
            holder.lastConx?.text = chat.getLastUpdateConx()
            holder.itemView.setOnClickListener {
                Log.d("click chatid: ", chat.getId())
                val intent = Intent(mContext, ChatActivity::class.java)
                intent.putExtra("chatId", chat.getId())
                try {
                    mContext?.startActivity(intent)
                } catch (ActivityNotFoundException: Exception) {
                    Log.d("ImplicitIntents", "Can't handle this!")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        if (data != null) {
            return data.size
        }
        return 0
    }

}