package com.example.pangchat.contact

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.PersonalActivity
import com.example.pangchat.R
import com.example.pangchat.contact.ContactAdapter.ContactViewHolder
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class ContactAdapter(private val mContext: FragmentActivity?, private val data: LinkedList<Contact?>?) : RecyclerView.Adapter<ContactViewHolder?>() {

    // 完成类ContactViewHolder
    // 使用itemView.findViewById()方法来寻找对应的控件

    class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = itemView.findViewById<ImageView?>(R.id.avatar_icon)
        var nickname: TextView? = itemView.findViewById<TextView?>(R.id.nickname_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {

        val contact = data?.get(position)
        if (contact != null) {

            // holder.avatar?.setImageResource(contact.getAvatar())
            mContext?.lifecycleScope?.launch{
                // 发起下载图片请求
                if (!webSocketClient.urlToBitmap.containsKey(contact.getAvatar())){
                    val bit: Bitmap;
                    withContext(Dispatchers.IO) {
                        val result = CookiedFuel.get(contact.getAvatar()).awaitByteArray();
                        bit = BitmapFactory.decodeByteArray(result, 0, result.size)
                        webSocketClient.urlToBitmap[contact.getAvatar()] = bit
                    }
                }
                holder.avatar?.setImageBitmap(webSocketClient.urlToBitmap[contact.getAvatar()]) // 必须放在IO外面
            }

            holder.nickname?.text = contact.getNickname()
            holder.itemView.setOnClickListener{
                val contact : Contact = data?.get(position)!!

                // Log.d("click userId: ", contact.getUserId())

                val intent = Intent(mContext, PersonalActivity::class.java)
                // intent.putExtra("myUserId", mContext?.intent?.getStringExtra("userId"))
                intent.putExtra("friendNames", mContext?.intent?.getStringArrayListExtra("friendNames"))
                intent.putExtra("userId", contact.getUserId())
                intent.putExtra("nickname", contact.getNickname())
                intent.putExtra("username", contact.getUsername())
                intent.putExtra("avatar", contact.getAvatar())

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