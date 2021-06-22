package com.example.pangchat.message

import android.content.Intent
import android.graphics.Bitmap
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.ChatActivity
import com.example.pangchat.PersonalActivity
import com.example.pangchat.R
import com.example.pangchat.message.data.*
import java.util.*

class MessageAdapter(private val myUserId: String, private val activity: ChatActivity,
                     private val data: LinkedList<Message?>?, private val urlToBitmap: MutableMap<String, Bitmap>) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {
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
        var type: Int = 0
        when(data?.get(position)?.getType()) {
            "text" -> {
                type = 0
            }
            "image" -> {
                type = 1
            }
            "video" -> {
                type = 2
            }
            "audio" -> {
                type = 3
            }
            "location" -> {
                type = 4
            }
        }

        return send * 5 - 2 + type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        val messageLayout = intArrayOf(
            R.layout.item_message_deleted,
            R.layout.item_message_receive_recalled,
            R.layout.item_message_send_recalled,
            R.layout.item_message_receive_0text,
            R.layout.item_message_receive_1image,
            R.layout.item_message_receive_2video,
            R.layout.item_message_receive_3audio,
            R.layout.item_message_receive_4location,
            R.layout.item_message_send_0text,
            R.layout.item_message_send_1image,
            R.layout.item_message_send_2video,
            R.layout.item_message_send_3audio,
            R.layout.item_message_send_4location
            )
        view = LayoutInflater.from(parent.context).inflate(messageLayout[viewType], parent, false)
        return MessageViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = data?.get(position)

        val viewHolder = holder as MessageViewHolder
        if (message != null) {
            // FIXME: 增加 popup
            if(!message.isBlocked(myUserId)){
                if(viewHolder.viewType > 2){
                    if(!urlToBitmap.keys.contains(message.getAvatar())){
                        viewHolder.avatar?.setImageBitmap(urlToBitmap[message.getAvatar()])
                    }
                    /*
                    if(avatarBitmaps != null && avatarBitmaps?.size > position && avatarBitmaps?.get(position) != null){
                        viewHolder.avatar?.setImageBitmap(avatarBitmaps?.get(position))
                    }

                     */
                    viewHolder.avatar?.setOnClickListener {
                        val intent = Intent(activity, PersonalActivity::class.java)
                        intent.putExtra("userId", message.getSenderId())
                        intent.putExtra("username", message.getUsername())
                        intent.putExtra("avatar", message.getAvatar())
                        try {
                            activity.startActivity(intent)
                        } catch (ActivityNotFoundException: Exception) {
                            Log.d("ImplicitIntents", "Can't handle this!")
                        }
                    }

                    viewHolder.nickname?.text = message.getUsername()
                    viewHolder.content?.text = message.getContent()

                    // FIXME: popup
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
                    viewHolder.messageDelete?.setOnClickListener {
                        activity.deleteMessage(position, message.getId(), myUserId)
                        viewHolder.messageAction?.visibility = View.GONE
                    }

                    // TODO: 各种特定的跳转和设置
                    when((viewHolder.viewType - 3) % 5){
                        1 ->{
                            if(urlToBitmap.keys.contains(message.getContent())){
                                viewHolder.messageImage?.setImageBitmap(urlToBitmap[message.getContent()])
                            }else{
                                activity.downLoadImageBitmap(message.getContent())
                            }
                        }
                        // 2: video
                        2 ->{

                        }
                        // 3: audio
                        3 ->{

                        }
                        // 4: location
                        4 ->{
                            val location = message.getContent()
                            val latitude = location.substring(0, location.indexOf(';')).toDouble()
                            val longitude = location.substring(location.indexOf(';') + 1).toDouble()
                            val geocoder = Geocoder(activity)
                            try{
                                val addresses = geocoder.getFromLocation(latitude, longitude, 1)
                                if(addresses.size > 0){
                                    val address = addresses[0].countryName + addresses[0].locality +
                                            addresses[0].subLocality + addresses[0].subAdminArea
                                    viewHolder.content?.text = address
                                }
                            }catch(e : Exception){

                            }
                            viewHolder.messageBlock?.setOnClickListener {
                                val mapIntent: Intent = Uri.parse(
                                    "geo:$latitude, $longitude"
                                ).let{
                                        location ->
                                    Intent(Intent.ACTION_VIEW, location)
                                }

                                val intentChooser = Intent.createChooser(mapIntent, "选择地图")
                                try{
                                    activity.startActivity(intentChooser)
                                }catch (ActivityNotFoundException: Exception){
                                    Log.d("ImplicitIntents", "Can't handle this!")
                                }
                            }
                        }
                    }

                }else{
                    var recallMsg: String = message.getUsername()
                    if(message.getSenderId().compareTo(myUserId) == 0){
                        recallMsg = "您"
                    }
                    recallMsg += activity.getString(R.string.messageRecalled)
                    viewHolder.recalledInfo?.text = recallMsg
                    if(message.getSenderId().compareTo(myUserId) != 0 || message.getType().compareTo("text") != 0){
                        viewHolder.recalledReedit?.visibility = View.GONE
                    }else {
                        viewHolder.recalledReedit?.visibility = View.VISIBLE
                        viewHolder.recalledReedit?.setOnClickListener {
                            Log.d("recalled reedit ", "clicked")
                            activity.setInput(message.getContent());
                        }
                    }
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

    class MessageViewHolder(itemView: View, var viewType: Int) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView? = null
        var nickname: TextView? = null
        var content: TextView? = null

        var messageBlock: LinearLayout? = null
        var messageImage: ImageView? = null

        var messageAction: LinearLayout? = null
        var messageCopy: TextView? = null
        var messageRecall: TextView? = null
        var messageDelete: TextView? = null

        var recalledInfo: TextView? = null
        var recalledReedit: TextView? = null

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
                    if ((viewType - 3) % 5 == 1){
                        messageImage = itemView.findViewById(R.id.messageImage)
                    }else if((viewType - 3) % 5 > 1){
                        messageBlock = itemView.findViewById(R.id.messageBlock)
                    }
                }else{
                    recalledInfo = itemView.findViewById(R.id.recalledInfo)
                    recalledReedit = itemView.findViewById(R.id.recalledReedit)
                }
            }
        }
    }
}