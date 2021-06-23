package com.example.pangchat.chat

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.ChatInfoActivity
import com.example.pangchat.PersonalActivity
import com.example.pangchat.R
import com.example.pangchat.SelectFriendsActivity
import com.example.pangchat.message.data.*
import com.example.pangchat.user.User
import java.lang.reflect.Member
import java.util.*

class ChatMemberAdapter(private val activity: ChatInfoActivity, private val data: LinkedList<User?>, private val urlToBitmap: MutableMap<String, Bitmap>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {
        return if(position < data.size - 1){
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_avatar_name, parent, false)
        return MemberViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO
        val user = data.get(position)

        val viewHolder = holder as MemberViewHolder
        if (user != null) {
            if (viewHolder.viewType == 0) {
                viewHolder.username.text = user.getUsername()

                if(urlToBitmap.keys.contains(user.getAvatar())){
                    viewHolder.avatar.setImageBitmap(urlToBitmap[user.getAvatar()])
                }

                viewHolder.block.setOnClickListener {
                    val intent = Intent(activity, PersonalActivity::class.java)
                    intent.putExtra("userId", user.getUserId())
                    intent.putExtra("username", user.getUsername())
                    intent.putExtra("avatar", user.getAvatar())

                    try {
                        activity.startActivity(intent)
                    } catch (ActivityNotFoundException: Exception) {
                        Log.d("ImplicitIntents", "Can't handle this!")
                    }
                }
            } else {
                viewHolder.avatar.setImageResource(R.drawable.outline_add_box_grey_400_48dp)
                viewHolder.avatar.setOnClickListener {
                    activity.toAddChatMember()
                }
            }
            // TODO: 如果要做删除成员要加在这里
        }
    }


    override fun getItemCount(): Int {
        return data.size
    }

    class MemberViewHolder(itemView: View, var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var block: LinearLayout = itemView.findViewById(R.id.avatarAndUsername)
        var avatar: ImageView = itemView.findViewById(R.id.avatar)
        var username: TextView = itemView.findViewById(R.id.username)
    }
}