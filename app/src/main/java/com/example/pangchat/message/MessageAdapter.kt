package com.example.pangchat.message

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R
import com.example.pangchat.message.data.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class MessageAdapter(private val data: LinkedList<String>?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {
        // TODO
        // return number of images in the post
       //  return data?.get(position)!!.getMessageType()
        // FIXME: 假设目前全部都是 text。之后应该要考虑 1. 是接收方还是发出方 2.
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // TODO
        val view: View?
        val messageLayout = intArrayOf(R.layout.item_message_receive)
        view = LayoutInflater.from(parent.context).inflate(messageLayout[viewType], parent, false)
        return MessageViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO
        val messageId = data?.get(position)

        /*
        MainScope().launch {

            val result: Result<MessageInfo>
            val messageRequest = MessageRequest()

            withContext(Dispatchers.IO) {
                if(messageId != null){
                    result = messageRequest.getMessage(messageId)
                    if (result is Result.Success) {
                        //_loginResult.value = LoginResult(success = LoggedInUserView(displayName = result.data.username, userId = result.data.userId))
                        val message = Message(result.data.messageId, result.data.senderId, result.data.nickname, result.data.avatarIcon, result.data.recalled, result.data.content, result.data.time)
                        val viewHolder = holder as MessageViewHolder
                        // viewHolder.avatar = message.getAvatarIcon()
                        // FIXME:

                        viewHolder.nickname.text = message.getNickname()
                        viewHolder.content.text = message.getContent()
                        viewHolder.time.text = message.getTime()
                    } else {
                        // _loginResult.value = LoginResult(error = R.string.login_failed)
                    }
                }
            }


        }

         */
    }


    override fun getItemCount(): Int {
        // TODO
        if (data != null) {
            return data.size
        }
        return 0
    }

    // TODO: 完成MessageViewHolder类
    class MessageViewHolder(itemView: View, var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView
        var nickname: TextView
        var content: TextView
        var time: TextView
        // var imgs: Array<ImageView?>? = null

        // TODO: 添加其他包含的其他控件
        init {
            avatar = itemView.findViewById<ImageView?>(R.id.avatar_icon)
            nickname = itemView.findViewById<TextView?>(R.id.nickname_text)
            content = itemView.findViewById<TextView?>(R.id.message_content)
            time = itemView.findViewById<TextView?>(R.id.message_time)
            // TODO: 根据不同 view type 绑定更多不同部件
            /*
            if (imageCount != 0) {
                val imgView = intArrayOf(R.id.post_image_0, R.id.post_image_1, R.id.post_image_2, R.id.post_image_3)
                imgs = arrayOfNulls<ImageView?>(imageCount)
                for (i in 0 until imageCount) {
                    imgs!![i] = itemView.findViewById<ImageView?>(imgView[i])
                }
            }

             */
        }
    }

}