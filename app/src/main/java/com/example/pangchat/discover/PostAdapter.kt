package com.example.pangchat.discover

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.*
import com.example.pangchat.comment.CommentAdapter
import com.example.pangchat.websocketClient.webSocketClient
import com.google.android.material.textfield.TextInputEditText
import java.util.*


class PostAdapter(private val activity: MainActivity, private val fragment: DiscoverFragment, var data: LinkedList<Post?>?) : RecyclerView.Adapter<RecyclerView.ViewHolder?>() {
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

    private fun showComment(post:Post){
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
                    post.getId()?.let { it1 -> comment_text?.let { it2 ->
                        fragment.commentPostFun(it1,
                            it2
                        )
                    } }
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

    private fun showMore(moreBtnView:View,post: Post){
        val like: TextView? = popView?.findViewById<TextView>(R.id.like)
        if(MorePopupWindow==null){
            MorePopupWindow = PopupWindow(popView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            MorePopupWindow!!.setOutsideTouchable(true);
            MorePopupWindow!!.setTouchable(true);
            MorePopupWindow!!.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
            val comment = popView?.findViewById<TextView>(R.id.comment)
            like?.setOnClickListener {
                if(like.text=="赞")
                {
                    post.getId()?.let { it1 -> fragment.likePostFun(it1,like) }
                }
                else{
                    post.getId()?.let { it1 -> fragment.canceLikePostFun(it1,like) }
                }

            }
            comment?.setOnClickListener {
                showComment(post)
            }

        }
        if(post.getLikeIds()?.contains(webSocketClient.userId) == true){
            if (like != null) {
                like.text ="取消"
            }
        }else{
            if (like != null) {
                like.text = "赞"
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
        System.out.println("before bind" + System.currentTimeMillis())
        // TODO
        val post = data?.get(position)
        val viewHolder = holder as DiscoverViewHolder
        if (post != null) {

            if( webSocketClient.urlToBitmap.keys.contains(post.getAvatarIcon())){
                viewHolder.avatar?.setImageBitmap(webSocketClient.urlToBitmap[post.getAvatarIcon()])
            }else{
                fragment.downLoadImageBitmap(post.getAvatarIcon())
            }
            viewHolder.nickname?.text = post.getNickname()
            viewHolder.content?.text = post.getText()
            viewHolder.postTime?.text = post.getPublishedTime()
            if(post.getLikes().isNullOrEmpty()){
                viewHolder.Likes?.visibility = View.GONE
            }

            viewHolder.comment?.adapter = CommentAdapter(fragment,post.getComments())
            val _linearLayoutManager = LinearLayoutManager(this.activity)
            _linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
            viewHolder.comment?.layoutManager = _linearLayoutManager

            if(!post.getLikes().isNullOrEmpty()){
            viewHolder.Likes?.text = post.getLikes()?.let { TextUtils.join(", ", it) }
            viewHolder.Likes?.visibility = View.VISIBLE
            }
            val viewType = getItemViewType(position)
            viewHolder.moreButton?.setOnClickListener(View.OnClickListener {
                showMore(viewHolder.moreButton!!,post)
            })

            for (i in 0 until viewType) {
                val url = post.getImages()!![i];
                if(url !=null&&post.getType().equals("image")){
                    if( webSocketClient.urlToBitmap.keys.contains(url)){
                        viewHolder.imgs?.get(i)?.setImageBitmap(webSocketClient.urlToBitmap[url])
                    }else{
                        fragment.downLoadImageBitmap(url)
                    }
                }
                if(url!=null&&post.getType().equals("video")){
                    viewHolder.imgs?.get(i)?.setImageResource(R.drawable.round_play_circle_outline_grey_48dp)
                    viewHolder.imgs?.get(i)?.setBackgroundColor(android.graphics.Color.parseColor("#A4A4A4"));
                    viewHolder.imgs?.get(i)?.setOnClickListener {
                        val intent = Intent(activity, VideoPlayActivity::class.java)
                        intent.putExtra("videoUrl", url)
                        try {
                            activity.startActivity(intent)
                        } catch (ActivityNotFoundException: Exception) {
                            Log.d("ImplicitIntents", "Can't handle this!")
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        // TODO
        if (data != null) {
            return data!!.size
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
        var comment:RecyclerView?
        var moreButton: Button? = null

        // TODO: 添加其他包含的其他控件
        init {
            avatar = itemView.findViewById<ImageView?>(R.id.avatar_icon)
            nickname = itemView.findViewById<TextView?>(R.id.nickname_text)
            content = itemView.findViewById<TextView?>(R.id.post_content)
            postTime = itemView.findViewById<TextView?>(R.id.post_time)
            Likes = itemView.findViewById<TextView?>(R.id.post_like)
            comment = itemView.findViewById<RecyclerView?>(R.id.comment_listview)
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