package com.example.pangchat.contact

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R
import com.example.pangchat.contact.SelectFriendsAdapter.SelectFriendsViewHolder
import java.util.*

class SelectFriendsAdapter(private val data: LinkedList<Contact?>?) : RecyclerView.Adapter<SelectFriendsViewHolder?>() {

    // 完成类ContactViewHolder
    // 使用itemView.findViewById()方法来寻找对应的控件
    // TODO
    class SelectFriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = itemView.findViewById<ImageView?>(R.id.avatar_icon)
        var nickname: TextView? = itemView.findViewById<TextView?>(R.id.nickname_text)
        var button: RadioButton? = itemView.findViewById<RadioButton>(R.id.is_checked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectFriendsViewHolder {
        // TODO
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_select_friends, parent, false)
        return SelectFriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectFriendsViewHolder, position: Int) {
        // TODO
        val contact = data?.get(position)
        if (contact != null) {
            holder.avatar?.setImageResource(contact.getAvatarIcon())
            holder.nickname?.text = contact.getNickname()
        }

        holder.avatar?.setOnClickListener(View.OnClickListener {
            holder.button?.isChecked = !holder.button?.isChecked!!
        })

        holder.nickname?.setOnClickListener(View.OnClickListener {
            holder.button?.isChecked = !holder.button?.isChecked!!
        })

        holder.button?.setOnClickListener(View.OnClickListener {
            holder.button?.isChecked = !holder.button?.isChecked!!
        })
    }

    override fun getItemCount(): Int {
        // TODO
        if (data != null) {
            return data.size
        }
        return 0
    }

}