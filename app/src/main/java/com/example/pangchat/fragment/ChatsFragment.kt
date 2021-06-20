package com.example.pangchat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.ChatAdapter
import com.example.pangchat.user.data.UserChats
import com.example.pangchat.user.data.UserRequest
import com.example.pangchat.user.data.UserResult
import com.example.pangchat.websocketClient.webSocketClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {
    private var chatAdapter: ChatAdapter? = null
    private var data: LinkedList<Chat?>? = null
    private var recyclerView: RecyclerView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        recyclerView = view.findViewById<RecyclerView?>(R.id.chat_recyclerview)


        data = LinkedList()
        chatAdapter = ChatAdapter(activity, data)
        recyclerView?.adapter = chatAdapter


        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager

        lifecycleScope.launch{
            var chats : ArrayList<Chat>? = getUserChats()
            for(chat in chats!!){
                data?.add(chat)
            }
            recyclerView?.adapter?.notifyDataSetChanged()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_chats, container, false)
    }

    suspend fun getUserChats(): ArrayList<Chat>? {
        val userRequest = UserRequest()
        val result: UserResult<UserChats>

        withContext(Dispatchers.IO) {
            result = userRequest.getUserChats(webSocketClient.username!!)
        }

        if (result is UserResult.Success) {
            return result.data.chats
        }
        return null
        // TODO: 解析异常
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