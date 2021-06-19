package com.example.pangchat.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.ChatActivity
import com.example.pangchat.R
import com.example.pangchat.message.data.*
import kotlinx.coroutines.launch
import java.util.*

class MessageAdapter(private val activity: ChatActivity, private val data: LinkedList<Message?>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {

        // FIXME: 假设目前全部都是 text。之后应该要考虑 1. 是接收方还是发出方 2. 哪种类型 3. 是否recalled

        var send: Int = 0
        if(data?.get(position)?.getNickname()?.compareTo("pwf") == 0){
            send = 1
        }

        if(data?.get(position)?.getRecalled() == true){
            return send
        }
        return 2 + send
        // return send * 一种模板总量 + 种类
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        // TODO: 增加更多布局
        val messageLayout = intArrayOf( R.layout.item_message_receive_recalled, R.layout.item_message_send_recalled, R.layout.item_message_receive_0text, R.layout.item_message_send_0text, )
        view = LayoutInflater.from(parent.context).inflate(messageLayout[viewType], parent, false)
        return MessageViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO
        val message = data?.get(position)

        val viewHolder = holder as MessageViewHolder
        if (message != null) {
            // FIXME: 增加 popup
            if(viewHolder.viewType > 1){
                viewHolder.nickname?.text = message.getNickname()
                viewHolder.content?.text = message.getContent()
                viewHolder.content?.setOnLongClickListener {
                    Log.d("long click ", position.toString())
                    activity.recallMessage(position, message.getMessageId(), message.getNickname())
                    return@setOnLongClickListener true
                }
            }else{
                var recallUser: String = message.getNickname()
                if(recallUser.compareTo("pwf") == 0){
                    recallUser = "您"
                }
                viewHolder.recalledInfo?.text = recallUser + activity.getString(R.string.messageRecalled)
                viewHolder.recalledReedit?.setOnClickListener{
                    Log.d("recalled reedit ", "clicked")
                    // TODO: 只有文字信息可重新编辑
                    activity.setInput(message.getContent());
                }
            }

            // viewHolder.time.text = message.getTime()
        }
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
        var avatar: ImageView? = null
        var nickname: TextView? = null
        var content: TextView? = null
        // var time: TextView
        // var imgs: Array<ImageView?>? = null

        var recalledInfo: TextView? = null
        var recalledReedit: TextView? = null

        // TODO: 添加其他包含的其他控件
        init {
            if(viewType > 1){
                avatar = itemView.findViewById<ImageView?>(R.id.avatar_icon)
                nickname = itemView.findViewById<TextView?>(R.id.nickname_text)
                content = itemView.findViewById<TextView?>(R.id.message_content)
            }else{
                recalledInfo = itemView.findViewById(R.id.recalledInfo)
                recalledReedit = itemView.findViewById(R.id.recalledReedit)
            }
            // TODO: 根据不同 view type 绑定更多不同部件

        }
    }

}