package com.example.pangchat.comment

import android.annotation.SuppressLint
import android.content.Context
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
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.pangchat.R
import com.example.pangchat.comment.Comment
import java.util.*

class CommentAdapter (private val data: LinkedList<Comment?>?) : BaseAdapter() {
    override fun getCount(): Int {
        if (data != null) {
            return data.size
        }
        return 0
    }

    override fun getItem(position: Int): Any? {
        if (data != null) {
            return data[position]
        }
        return null
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
        var convertView = convertView
        if (parent != null) {
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_list_comment, parent, false)
        }
        val comment = data?.get(position)
        // 修改View中各个控件的属性，使之显示对应位置Chat的内容
        // 使用convertView.findViewById()方法来寻找对应的控件
        // 控件ID见 res/layout/item_list_chat.xml
        // TODO
        val comment_view = convertView?.findViewById<TextView?>(R.id.comment_text)
        if(comment != null){
            val nickname:String = comment.getNickname()
            val content:String = comment.getText()
            val span = SpannableString(nickname+content)
            //设置字体前景色
            span.setSpan( ForegroundColorSpan(Color.parseColor("#4d88ff")), 0, nickname.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            span.setSpan(StyleSpan(Typeface.BOLD), 0, nickname.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE) //正常
            span.setSpan(AbsoluteSizeSpan(40),0,nickname.length,Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            if (comment_view != null) {
                comment_view.setText(span)
            }
            return convertView
        }
        return null
    }

}