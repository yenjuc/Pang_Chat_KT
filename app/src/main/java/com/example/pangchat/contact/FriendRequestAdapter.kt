package com.example.pangchat.contact

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.MainActivity
import com.example.pangchat.R
import com.example.pangchat.fragment.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class FriendRequestAdapter(private val mContext: FragmentActivity?, private val data: LinkedList<Contact?>?) : RecyclerView.Adapter<FriendRequestAdapter.FriendRequestViewHolder?>() {

    private val _acceptResult = MutableLiveData<AddFriendResult>()

    class FriendRequestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView = itemView.findViewById<ImageView?>(R.id.avatar_icon)
        var nickname: TextView = itemView.findViewById<TextView?>(R.id.nickname_text)
        var acceptButton: Button = itemView.findViewById<RadioButton>(R.id.accept)
        var refuseButton: Button = itemView.findViewById<RadioButton>(R.id.refuse)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestViewHolder {
        // TODO
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycle_new_friends, parent, false)
        return FriendRequestViewHolder(view)
    }

    override fun onBindViewHolder(holder: FriendRequestViewHolder, position: Int) {
        // TODO
        val contact = data?.get(position)
        if (contact != null) {
            holder.avatar.setImageResource(contact.getAvatar())
            holder.nickname.text = contact.getNickname()
            holder.acceptButton.setOnClickListener(View.OnClickListener {
                // 接受好友申请 -- 发送请求给数据库，成功后重新推进联系人页面
                mContext?.lifecycleScope?.launch {
                    contact.getUserId().let { it1 -> accept(it1) }

                    val intent = Intent(mContext, MainActivity::class.java)
                    intent.putExtra("fragment", "contact")

                    try {
                        mContext.startActivity(intent)
                    } catch (ActivityNotFoundException: Exception) {
                        Log.d("ImplicitIntents", "Can't handle this!")
                    }

                }
            })
            holder.refuseButton.setOnClickListener(View.OnClickListener {
                // 拒绝好友申请 -- 发送请求给数据库，成功后重新推进联系人页面
                mContext?.lifecycleScope?.launch {
                    contact.getUserId().let { it1 -> refuse(it1) }

                    val intent = Intent(mContext, MainActivity::class.java)
                    intent.putExtra("fragment", "contact")

                    try {
                        mContext.startActivity(intent)
                    } catch (ActivityNotFoundException: Exception) {
                        Log.d("ImplicitIntents", "Can't handle this!")
                    }

                }
            })
        }

    }

    override fun getItemCount(): Int {
        // TODO
        if (data != null) {
            return data.size
        }
        return 0
    }

    suspend fun accept(friendId: String) {
        val contactDataSource = ContactDataSource()

        val result: Result<AddFriendResult>

        withContext(Dispatchers.IO) {
            result = contactDataSource.acceptNewFriend(friendId)
        }

        if (result is Result.Success) {
            _acceptResult.value = result.data
        } else {
            // TODO：抛出并解析异常
        }

    }

    suspend fun refuse(friendId: String) {
        val contactDataSource = ContactDataSource()

        val result: Result<AddFriendResult>

        withContext(Dispatchers.IO) {
            result = contactDataSource.refuseNewFriend(friendId)
        }

        if (result is Result.Success) {
            _acceptResult.value = result.data
        } else {
            // TODO：抛出并解析异常
        }

    }

}