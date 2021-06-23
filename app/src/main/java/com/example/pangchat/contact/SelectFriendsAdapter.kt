package com.example.pangchat.contact

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R
import com.example.pangchat.contact.SelectFriendsAdapter.SelectFriendsViewHolder
import com.example.pangchat.utils.CookiedFuel
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class SelectFriendsAdapter(private val mContext: FragmentActivity?, private val data: LinkedList<Contact?>?) : RecyclerView.Adapter<SelectFriendsViewHolder?>() {

    class SelectFriendsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = itemView.findViewById<ImageView?>(R.id.avatar_icon)
        var nickname: TextView? = itemView.findViewById<TextView?>(R.id.nickname_text)
        var button: RadioButton? = itemView.findViewById<RadioButton>(R.id.is_checked)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectFriendsViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_select_friends, parent, false)
        return SelectFriendsViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectFriendsViewHolder, position: Int) {

        val contact = data?.get(position)
        if (contact != null) {

            // holder.avatar?.setImageResource(contact.getAvatar())
            mContext?.lifecycleScope?.launch{
                // 发起下载图片请求
                val bit: Bitmap;
                withContext(Dispatchers.IO) {
                    val result = CookiedFuel.get(contact.getAvatar()).awaitByteArray();
                    bit = BitmapFactory.decodeByteArray(result, 0, result.size)
                }
                holder.avatar?.setImageBitmap(bit) // 必须放在IO外面
            }

            holder.nickname?.text = contact.getNickname()
            holder.itemView.setOnClickListener(View.OnClickListener {
                holder.button?.isChecked = !holder.button?.isChecked!!
                if (holder.button?.isChecked == true) {
                    // 这里传入昵称
                    mContext?.intent?.getStringArrayListExtra("selectedNames")?.add(contact.getNickname())
                    mContext?.intent?.getStringArrayListExtra("selectedIds")?.add(contact.getUserId())
                }
                else {
                    mContext?.intent?.getStringArrayListExtra("selectedNames")?.remove(contact.getNickname())
                    mContext?.intent?.getStringArrayListExtra("selectedIds")?.remove(contact.getUserId())
                }
            })
        }



//        holder.nickname?.setOnClickListener(View.OnClickListener {
//            holder.button?.isChecked = !holder.button?.isChecked!!
//        })
//
//        holder.button?.setOnClickListener(View.OnClickListener {
//            holder.button?.isChecked = !holder.button?.isChecked!!
//        })
    }

    override fun getItemCount(): Int {
        // TODO
        if (data != null) {
            return data.size
        }
        return 0
    }

}