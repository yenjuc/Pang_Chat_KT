package com.example.pangchat.comment

import android.graphics.Color
import android.graphics.Typeface
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.DiscoverFragment
import com.example.pangchat.R
import java.util.*
import kotlin.collections.ArrayList

class CommentAdapter(private val fragment: DiscoverFragment, private val data: LinkedList<Comment?>?) : RecyclerView.Adapter<CommentAdapter.CommentViewHolder?>() {

    class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var comment:TextView? = null
        init {
            comment = itemView.findViewById<TextView?>(R.id.comment_text)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):CommentAdapter.CommentViewHolder {
        // TODO
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_comment, parent, false)
        return CommentViewHolder(view)
    }

    override fun onBindViewHolder(holder: CommentAdapter.CommentViewHolder, position: Int) {
        // TODO
        val comment = data?.get(position)
        if (comment != null) {
            val comment_view = holder.comment
            if (comment != null) {
                val nickname: String = comment.getNickname()
                val start: String = nickname + ": "
                val content: String = comment.getText()
                val span = SpannableString(start + content)
                //设置字体前景色
                span.setSpan(
                    ForegroundColorSpan(Color.parseColor("#4d88ff")),
                    0,
                    start.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                );
                span.setSpan(
                    StyleSpan(Typeface.BOLD),
                    0,
                    start.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                ) //正常
                span.setSpan(
                    AbsoluteSizeSpan(40),
                    0,
                    start.length,
                    Spanned.SPAN_INCLUSIVE_EXCLUSIVE
                )
                if (comment_view != null) {
                    comment_view.setText(span)
                }
//            holder.avatar?.setImageResource(contact.getAvatar())
//            holder.nickname?.text = contact.getNickname()
//            holder.itemView.setOnClickListener{
//                val contact : Contact = data?.get(position)!!
//
//                // Log.d("click userId: ", contact.getUserId())
//
//                val intent = Intent(mContext, PersonalActivity::class.java)
//                // intent.putExtra("myUserId", mContext?.intent?.getStringExtra("userId"))
//                intent.putExtra("friendNames", mContext?.intent?.getStringArrayListExtra("friendNames"))
//                intent.putExtra("userId", contact.getUserId())
//                intent.putExtra("username", contact.getNickname())
//                intent.putExtra("avatar", contact.getAvatar())
//
//                try {
//                    mContext?.startActivity(intent)
//                } catch (ActivityNotFoundException: Exception) {
//                    Log.d("ImplicitIntents", "Can't handle this!")
//                }
//            }
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


//class CommentAdapter (private val data: ArrayList<Comment?>?) : BaseAdapter() {
//    override fun getCount(): Int {
//        if (data != null) {
//            return data.size
//        }
//        return 0
//    }
//
//    override fun getItem(position: Int): Any? {
//        if (data != null) {
//            return data[position]
//        }
//        return null
//    }
//
//    override fun getItemId(position: Int): Long {
//        return 0
//    }
//
//    @SuppressLint("ViewHolder")
//    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
//        var convertView = convertView
//        if (parent != null) {
//            convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_comment, parent, false)
//        }
//        val comment = data?.get(position)
//        // 修改View中各个控件的属性，使之显示对应位置Chat的内容
//        // 使用convertView.findViewById()方法来寻找对应的控件
//        // 控件ID见 res/layout/item_list_chat.xml
//        // TODO
//        val comment_view = convertView?.findViewById<TextView?>(R.id.comment_text)
//        if(comment != null){
//            val nickname:String = comment.getNickname()
//            val start:String = nickname+": "
//            val content:String = comment.getText()
//            val span = SpannableString(start+content)
//            //设置字体前景色
//            span.setSpan( ForegroundColorSpan(Color.parseColor("#4d88ff")), 0, start.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//            span.setSpan(StyleSpan(Typeface.BOLD), 0, start.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE) //正常
//            span.setSpan(AbsoluteSizeSpan(40),0,start.length,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
//            if (comment_view != null) {
//                comment_view.setText(span)
//            }
//            return convertView
//        }
//        return null
//    }
//
//}