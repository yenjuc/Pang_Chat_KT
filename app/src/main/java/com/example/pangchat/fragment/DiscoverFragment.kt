package com.example.pangchat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.discover.Post
import com.example.pangchat.discover.PostAdapter
import com.example.pangchat.discover.data.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.example.pangchat.websocketClient.webSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var posts: LinkedList<Post?>? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val postFab = view.findViewById<FloatingActionButton>(R.id.post_fab)


        postFab.setOnClickListener (View.OnClickListener{
            val postIntent = Intent()
            this.activity?.let { it1 -> postIntent.setClass(it1,newPostActivity::class.java) }
            postIntent.putExtra("userId", webSocketClient.userId!!)
            try {
            this.activity?.startActivity(postIntent)
            } catch (e: Exception) {
                Log.d("ImplicitIntents", "Can't handle this!")
            }
        })


        recyclerView = view.findViewById(R.id.discover_recyclerview)

        if (recyclerView != null) {
            recyclerView!!.adapter = PostAdapter(activity as MainActivity,this,posts)
            recyclerView!!.layoutManager = LinearLayoutManager(this.activity)
        }

        lifecycleScope.launch {
            posts = getAllPost()

            if (recyclerView != null) {
                recyclerView!!.adapter?.notifyDataSetChanged()
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        return inflater?.inflate(R.layout.fragment_discover, container, false)

    }

    suspend fun getAllPost(): LinkedList<Post?>? {
        val discoverRequest = DiscoverRequest()
        val result: DiscoverResult<postGetAllResult>

        withContext(Dispatchers.IO) {
            // FIXME: type
            result = discoverRequest.getAllPosts(webSocketClient.userId!!)
        }

        if (result is DiscoverResult.Success) {
            Toast.makeText(this.context, "获取动态", Toast.LENGTH_SHORT).show()
            return result.data.posts
        } else {
            Log.d("ERROR",result.toString())
            return null
        }
    }

    fun likePostFun(postId: String,like: TextView?){
        lifecycleScope.launch {
            if(likePost(postId)){
                getAllPost()
                if (like != null) {
                    like.text="取消"
                }
                recyclerView?.adapter?.notifyDataSetChanged()

            }
        }
    }

    fun canceLikePostFun(postId: String,like: TextView?){
        lifecycleScope.launch {
            if(cancelLikePost(postId)){
                getAllPost()
                if (like != null) {
                    like.text="赞"
                }
                recyclerView?.adapter?.notifyDataSetChanged()
            }
        }
    }

    fun commentPostFun(postId: String,content: String){
        lifecycleScope.launch {
            if(postComment(postId,content)){
                getAllPost()
                recyclerView?.adapter?.notifyDataSetChanged()
            }
        }
    }

    private suspend fun likePost(postId:String) : Boolean{
        val discoverRequest = DiscoverRequest()
        val result: DiscoverResult<postLikeResult>

        withContext(Dispatchers.IO) {
            // FIXME: type
            result = discoverRequest.likePost(webSocketClient.userId!!,postId)
        }

        if (result is DiscoverResult.Success) {
            Toast.makeText(this.context, "点赞成功", Toast.LENGTH_SHORT).show()
            return true
        } else {
            Log.d("ERROR",result.toString())
            return false
        }
    }


    private suspend fun cancelLikePost(postId:String) : Boolean{
        val discoverRequest = DiscoverRequest()
        val result: DiscoverResult<cancelLikeResult>

        withContext(Dispatchers.IO) {
            // FIXME: type
            result = discoverRequest.cancelLikePost(webSocketClient.userId!!,postId)
        }

        if (result is DiscoverResult.Success) {
            Toast.makeText(this.context, "取消点赞成功", Toast.LENGTH_SHORT).show()
            return true
        } else {
            Log.d("ERROR",result.toString())
            return false
        }
    }

    private suspend fun postComment(postId: String,content: String) : Boolean{
        val discoverRequest = DiscoverRequest()
        val result: DiscoverResult<postCommentResult>

        withContext(Dispatchers.IO) {
            result = discoverRequest.commentPost(postId,webSocketClient.userId!!,content)
        }

        if (result is DiscoverResult.Success) {
            Toast.makeText(this.context, "评论", Toast.LENGTH_SHORT).show()
            return true
        } else {
            Log.d("ERROR",result.toString())
            return false
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ExploreFragment.
         */
        fun newInstance(): DiscoverFragment? {
            return DiscoverFragment()
        }
    }
}