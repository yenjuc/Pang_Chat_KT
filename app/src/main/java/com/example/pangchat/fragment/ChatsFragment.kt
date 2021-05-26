package com.example.pangchat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.ChatAdapter
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {
    // private lateinit var mContext: FragmentActivity
    private var chatAdapter: ChatAdapter? = null
    private var data: LinkedList<Chat?>? = null
    private var recyclerView: RecyclerView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById<RecyclerView?>(R.id.chat_recyclerview)
        // val context: Context? = activity

        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        // 添加数据的样例代码如下:
        // data = new LinkedList<>();
        // data.add(new Chat(getString(R.string.nickname1), R.drawable.avatar1, getString(R.string.sentence1), "2021/01/01"));
        // data.add(new Chat(getString(R.string.nickname2), R.drawable.avatar2, getString(R.string.sentence2), "2021/01/01"));
        // TODO
        data = LinkedList()
        data?.add(Chat("60ac6c49ebad580e2632520b",getString(R.string.nickname1), R.drawable.avatar1, getString(R.string.sentence1), "1995/12/30"))
        data?.add(Chat("60ac70310652f28934da7960",getString(R.string.nickname2), R.drawable.avatar2, getString(R.string.sentence2), "1990/01/13"))

        chatAdapter = ChatAdapter(activity, data)
        recyclerView?.adapter = chatAdapter


        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager
        
        
        // TODO: recyclerView?.setOnTouchListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_chats, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment MessageFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): ChatsFragment? {
            return ChatsFragment()
        }
    }
}