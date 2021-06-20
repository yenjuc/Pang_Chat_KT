package com.example.pangchat.contact

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.PersonalActivity
import com.example.pangchat.R
import com.example.pangchat.contact.ContactAdapter.ContactViewHolder
import java.util.*

class ContactAdapter(private val mContext: FragmentActivity?, private val data: LinkedList<Contact?>?) : RecyclerView.Adapter<ContactViewHolder?>() {

    // 完成类ContactViewHolder
    // 使用itemView.findViewById()方法来寻找对应的控件
    // TODO
    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = itemView.findViewById<ImageView?>(R.id.avatar_icon)
        var nickname: TextView? = itemView.findViewById<TextView?>(R.id.nickname_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        // TODO
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        // TODO
        val contact = data?.get(position)
        if (contact != null) {
            holder.avatar?.setImageResource(contact.getAvatarIcon())
            holder.nickname?.text = contact.getNickname()
            holder.itemView.setOnClickListener{
                val contact : Contact = data?.get(position)!!

                // Log.d("click userId: ", contact.getUserId())

                val intent = Intent(mContext, PersonalActivity::class.java)
                // intent.putExtra("myUserId", mContext?.intent?.getStringExtra("userId"))
                intent.putExtra("friendIds", mContext?.intent?.getStringArrayListExtra("friendIds"))
                intent.putExtra("userId", contact.getUserId())
                intent.putExtra("username", contact.getNickname())
                intent.putExtra("avatar", contact.getAvatarIcon())

                try {
                    mContext?.startActivity(intent)
                } catch (ActivityNotFoundException: Exception) {
                    Log.d("ImplicitIntents", "Can't handle this!")
                }
            }
        }
    }

    override fun getItemCount(): Int {
        // TODO
        if (data != null) {
            return data.size
        }
        return 0
    }

}