package com.example.pangchat.discover

import android.content.Context
import android.graphics.Color
import android.text.Layout
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.core.content.ContextCompat.getDrawable
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.MainActivity
import com.example.pangchat.R
import com.example.pangchat.afterTextChanged
import com.example.pangchat.comment.CommentAdapter
import com.example.pangchat.newPostActivity
import com.google.android.material.textfield.TextInputEditText
import java.security.AccessController.getContext
import java.util.*


class DiscoverAdapter(private val activity: MainActivity,private val data: LinkedList<Discover?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
    private var popView:View?=null
    private var MorePopupWindow: PopupWindow? = null
    private var commentPop:PopupWindow? = null
    private var commentPopView:View?=null
    private var view_parent:View?=null
    private var comment_edit: TextInputEditText?=null
    private var comment_text: String?=null

    override fun getItemViewType(position: Int): Int {
        // TODO
        // return number of images in the post
        return data?.get(position)!!.getImageCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        // TODO
        val view: View?
        val postLayout = intArrayOf(
            R.layout.item_post_no_img,
            R.layout.item_post_img1,
            R.layout.item_post_img2,
            R.layout.item_post_img3,
            R.layout.item_post_img4
        )
        view_parent=parent
        view = LayoutInflater.from(parent.context).inflate(postLayout[viewType], parent, false)
        popView = LayoutInflater.from(parent.context).inflate(R.layout.like_pop,parent,false)
        commentPopView = LayoutInflater.from(parent.context).inflate(R.layout.item_discover_comment,parent,false)

        val commentEditText = commentPopView?.findViewById<TextInputEditText>(R.id.discover_comment_input)


        return DiscoverViewHolder(view, viewType)
    }

    private fun showComment(){
            if(commentPop==null){
                commentPop = view_parent?.let {
                    PopupWindow(commentPopView,
                        it.width, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                };
                commentPop!!.setOutsideTouchable(true);
                commentPop!!.setTouchable(true);
                comment_edit = commentPopView?.findViewById<TextInputEditText>(R.id.discover_comment_input)
//                comment_edit?.onFocusChangeListener = View.OnFocusChangeListener() { view: View, b: Boolean ->
//                //当EditText失去焦点时，隐藏软键盘
//                    if (!b) {
//                        closeKeyBoard()
//                 }
//                };
                var send: Button? = commentPopView?.findViewById(R.id.button_send_comment)
                send?.isEnabled = false
                send?.setBackgroundColor(Color.parseColor("#EEEEEE"))


                commentPop!!.setOnDismissListener {
                    closeKeyBoard()
                }

                //TODO:发送评论
                comment_edit?.afterTextChanged{
                    comment_text = it
                    if(comment_text!=""){
                        send?.background= activity.getDrawable(R.drawable.btstyle)
                        send?.isEnabled=true
                    } else {
                        send?.isEnabled = false
                        send?.setBackgroundColor(Color.parseColor("#EEEEEE"))
                    }
                }

                send?.setOnClickListener {
                    //发送comment_text
                    closeKeyBoard()
                    comment_edit?.text?.clear()
                    commentPop!!.dismiss()
                }
            }

        if (commentPop!!.isShowing()) {
            commentPop!!.dismiss();
        } else {
            commentPop!!.showAtLocation(view_parent,Gravity.BOTTOM,0,0)

        }
    }

    //FIXME:这里还存在问题，attempted to finish an input event but the input event receiver has already been disposed.
    fun closeKeyBoard() {
        val inputMethodManager: InputMethodManager = activity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val result = inputMethodManager.hideSoftInputFromWindow(
            comment_edit?.getWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS
        )
        if(!result){
            Log.d("soft","failed")
        }
    }

    private fun showMore(moreBtnView:View){
        if(MorePopupWindow==null){
            MorePopupWindow = PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            MorePopupWindow!!.setOutsideTouchable(true);
            MorePopupWindow!!.setTouchable(true);
            MorePopupWindow!!.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
            val like: TextView? = popView?.findViewById<TextView>(R.id.like)
            val comment = popView?.findViewById<TextView>(R.id.comment)
            like?.setOnClickListener {
                // TODO:发送给后端信息
                if(like.text=="赞")
                {
                    like.text = "取消"
                }
                else{
                    like.text = "赞"
                }
            }
            comment?.setOnClickListener {
                showComment()
            }

        }
        if (MorePopupWindow!!.isShowing()) {
            MorePopupWindow!!.dismiss();
        } else {
            popView?.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            val mShowMorePopupWindowWidth = popView?.getMeasuredWidth();
            val mShowMorePopupWindowHeight = popView?.getMeasuredHeight();
                if (mShowMorePopupWindowHeight != null) {
                    MorePopupWindow!!.showAsDropDown(moreBtnView,-mShowMorePopupWindowWidth!!,
                        -(mShowMorePopupWindowHeight + moreBtnView.height) / 2)
                }
        }
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
            if(discover.getLikes()==null){
                viewHolder.Likes?.visibility = View.GONE
            }
            viewHolder.comment?.adapter = CommentAdapter(discover.getComments())
            viewHolder.Likes?.text = discover.getLikes()?.let { TextUtils.join(",", it) }
            val viewType = getItemViewType(position)
            viewHolder.moreButton?.setOnClickListener(View.OnClickListener {
                showMore(viewHolder.moreButton!!)
            })

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
        var Likes: TextView?
        var comment: ListView?
        var moreButton: Button? = null

        // TODO: 添加其他包含的其他控件
        init {
            avatar = itemView.findViewById<ImageView?>(R.id.avatar_icon)
            nickname = itemView.findViewById<TextView?>(R.id.nickname_text)
            content = itemView.findViewById<TextView?>(R.id.post_content)
            postTime = itemView.findViewById<TextView?>(R.id.post_time)
            Likes = itemView.findViewById<TextView?>(R.id.post_like)
            comment = itemView.findViewById<ListView?>(R.id.comment_listview)
            moreButton = itemView.findViewById<Button>(R.id.show_more)
            if (imageCount != 0) {
                val imgView = intArrayOf(
                    R.id.post_image_0,
                    R.id.post_image_1,
                    R.id.post_image_2,
                    R.id.post_image_3
                )
                imgs = arrayOfNulls<ImageView?>(imageCount)
                for (i in 0 until imageCount) {
                    imgs!![i] = itemView.findViewById<ImageView?>(imgView[i])
                }
            }
        }
    }
}