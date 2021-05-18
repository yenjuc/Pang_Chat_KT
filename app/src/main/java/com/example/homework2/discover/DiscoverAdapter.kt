package com.example.homework2.discover

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homework2.R
import java.util.*

class DiscoverAdapter(private val data: LinkedList<Discover?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    override fun getItemViewType(position: Int): Int {
        // TODO
        // return number of images in the post
        return data?.get(position)!!.getImageCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // TODO
        val view: View?
        val postLayout = intArrayOf(R.layout.item_post_no_img, R.layout.item_post_img1, R.layout.item_post_img2, R.layout.item_post_img3, R.layout.item_post_img4)
        view = LayoutInflater.from(parent.context).inflate(postLayout[viewType], parent, false)
        return DiscoverViewHolder(view, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // TODO
        val discover = data?.get(position)
        val viewHolder = holder as DiscoverViewHolder
        if (discover != null) {
            viewHolder.avatar?.setImageResource(discover.getAvatarIcon())
            viewHolder.nickname?.text = discover.getNickname()
            viewHolder.content?.text = discover.getText()
            viewHolder.postTime?.text = discover.getPublishedTime()
            val viewType = getItemViewType(position)

            for (i in 0 until viewType) {
                val id = discover.getImages()!![i];
                if(id !=null){
                    viewHolder.imgs?.get(i)?.setImageResource(id)
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

    // TODO: 完成DiscoverViewHolder类
    class DiscoverViewHolder(itemView: View, var imageCount: Int) : RecyclerView.ViewHolder(itemView) {
        var avatar: ImageView?
        var nickname: TextView?
        var content: TextView?
        var postTime: TextView?
        var imgs: Array<ImageView?>? = null

        // TODO: 添加其他包含的其他控件
        init {
            avatar = itemView.findViewById<ImageView?>(R.id.avatar_icon)
            nickname = itemView.findViewById<TextView?>(R.id.nickname_text)
            content = itemView.findViewById<TextView?>(R.id.post_content)
            postTime = itemView.findViewById<TextView?>(R.id.post_time)
            if (imageCount != 0) {
                val imgView = intArrayOf(R.id.post_image_0, R.id.post_image_1, R.id.post_image_2, R.id.post_image_3)
                imgs = arrayOfNulls<ImageView?>(imageCount)
                for (i in 0 until imageCount) {
                    imgs!![i] = itemView.findViewById<ImageView?>(imgView[i])
                }
            }
        }
    }

}