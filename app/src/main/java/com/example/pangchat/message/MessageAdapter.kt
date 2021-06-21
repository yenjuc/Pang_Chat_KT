package com.example.pangchat.message

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.ChatActivity
import com.example.pangchat.R
import com.example.pangchat.message.data.*
import com.example.pangchat.websocketClient.webSocketClient
import org.w3c.dom.Text
import java.util.*

class MessageAdapter(private val myUserId: String, private val activity: ChatActivity, private val data: LinkedList<Message?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {

        // FIXME: 假设目前全部都是 text。之后应该要考虑 1. 是接收方还是发出方 2. 哪种类型 3. 是否recalled

        if(data?.get(position)?.isBlocked(myUserId) == true){
            return 0
        }

        var send: Int = 1
        if(data?.get(position)?.getSenderId()?.compareTo(myUserId) == 0){
            send = 2
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
        val messageLayout = intArrayOf(R.layout.item_message_deleted, R.layout.item_message_receive_recalled,
            R.layout.item_message_send_recalled, R.layout.item_message_receive_0text, R.layout.item_message_send_0text, )
        view = LayoutInflater.from(parent.context).inflate(messageLayout[viewType], parent, false)
        return MessageViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO
        val message = data?.get(position)

        val viewHolder = holder as MessageViewHolder
        if (message != null) {
            // FIXME: 增加 popup
            if(!message.isBlocked(myUserId)){
                if(viewHolder.viewType > 2){
                    // FIXME: avatar 设置
                    viewHolder.nickname?.text = message.getUsername()
                    viewHolder.content?.text = message.getContent()
                    viewHolder.content?.setOnLongClickListener {
                        viewHolder.messageAction?.visibility = View.VISIBLE
                        return@setOnLongClickListener true
                    }
                    viewHolder.messageCopy?.setOnClickListener{
                        activity.setInput(message.getContent())
                        viewHolder.messageAction?.visibility = View.GONE
                    }
                    viewHolder.messageRecall?.setOnClickListener {
                        activity.recallMessage(position, message.getId())
                        viewHolder.messageAction?.visibility = View.GONE
                    }
                    // TODO: 删除消息

                }else{
                    var recallMsg: String = message.getUsername()
                    if(message.getSenderId().compareTo(myUserId) == 0){
                        recallMsg = "您"
                    }
                    recallMsg += activity.getString(R.string.messageRecalled)
                    viewHolder.recalledInfo?.text = recallMsg
                    viewHolder.recalledReedit?.setOnClickListener{
                        Log.d("recalled reedit ", "clicked")
                        // TODO: 只有文字信息可重新编辑
                        activity.setInput(message.getContent());
                    }
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

    // TODO: 完成MessageViewHolder类
    class MessageViewHolder(itemView: View, var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = null
        var nickname: TextView? = null
        var content: TextView? = null

        var messageAction: LinearLayout? = null
        var messageCopy: TextView? = null
        var messageRecall: TextView? = null
        var messageDelete: TextView? = null

        var recalledInfo: TextView? = null
        var recalledReedit: TextView? = null

        // TODO: 添加其他包含的其他控件
        init {
            if(viewType != 0){
                if(viewType > 2){
                    avatar = itemView.findViewById<ImageView?>(R.id.avatar_icon)
                    nickname = itemView.findViewById<TextView?>(R.id.nickname_text)
                    content = itemView.findViewById<TextView?>(R.id.message_content)
                    messageAction = itemView.findViewById(R.id.messageAction)
                    messageAction?.visibility = View.GONE
                    messageCopy = itemView.findViewById(R.id.messageCopy)
                    messageRecall = itemView.findViewById(R.id.messageRecall)
                    messageDelete = itemView.findViewById(R.id.messageDelete)
                }else{
                    recalledInfo = itemView.findViewById(R.id.recalledInfo)
                    recalledReedit = itemView.findViewById(R.id.recalledReedit)
                }
            }
        }
    }
}