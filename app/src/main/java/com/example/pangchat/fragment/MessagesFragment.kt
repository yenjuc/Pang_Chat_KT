package com.example.pangchat.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R

// FIXME: just for test, delete me afterward
import com.example.pangchat.contact.Contact
import com.example.pangchat.contact.ContactAdapter
import com.example.pangchat.message.MessageAdapter

import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [MessagesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MessagesFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.messages_recyclerView)

        // 添加数据，为recyclerView绑定Adapter、LayoutManager
        // 添加数据的样例代码如下:
        // LinkedList<Contact> contacts = new LinkedList<>();
        // contacts.add(new Contact(getString(R.string.nickname1), R.drawable.avatar1));
        // contacts.add(new Contact(getString(R.string.nickname2), R.drawable.avatar2));
        // FIXME: 目前用来测试，应该根据 chat_id 去拉取此聊天室的所有信息
        val chats = LinkedList<String>()
        chats.add("0")
        // chats.add("1")
        recyclerView?.adapter = MessageAdapter(chats);
        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_messages, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ContactsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): MessagesFragment? {
            return MessagesFragment()
        }
    }
}