package com.example.homework2

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import com.example.homework2.chat.Chat
import com.example.homework2.chat.ChatAdapter
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ChatsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatsFragment : Fragment() {
    private val chatAdapter: ChatAdapter? = null
    private var data: LinkedList<Chat?>? = null
    private var listView: ListView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        listView = getView()?.findViewById<ListView?>(R.id.listview)
        val context: Context? = activity

        // 向ListView 添加数据，新建ChatAdapter，并向listView绑定该Adapter
        // 添加数据的样例代码如下:
        // data = new LinkedList<>();
        // data.add(new Chat(getString(R.string.nickname1), R.drawable.avatar1, getString(R.string.sentence1), "2021/01/01"));
        // data.add(new Chat(getString(R.string.nickname2), R.drawable.avatar2, getString(R.string.sentence2), "2021/01/01"));
        // TODO
        data = LinkedList()
        data?.add(Chat(getString(R.string.nickname1), R.drawable.avatar1, getString(R.string.sentence1), "1995/12/30"))
        data?.add(Chat(getString(R.string.nickname2), R.drawable.avatar2, getString(R.string.sentence2), "1990/01/13"))
        data?.add(Chat(getString(R.string.nickname3), R.drawable.avatar3, getString(R.string.sentence3), "2009/06/21"))
        data?.add(Chat(getString(R.string.nickname4), R.drawable.avatar4, getString(R.string.sentence4), "2001/04/26"))
        data?.add(Chat(getString(R.string.nickname5), R.drawable.avatar5, getString(R.string.sentence5), "1982/07/03"))
        data?.add(Chat(getString(R.string.nickname6), R.drawable.avatar6, getString(R.string.sentence6), "1984/03/04"))
        data?.add(Chat(getString(R.string.nickname7), R.drawable.avatar7, getString(R.string.sentence7), "1999/11/11"))
        data?.add(Chat(getString(R.string.nickname8), R.drawable.avatar8, getString(R.string.sentence8), "2001/10/29"))
        data?.add(Chat(getString(R.string.nickname9), R.drawable.avatar9, getString(R.string.sentence9), "2000/04/16"))
        data?.add(Chat(getString(R.string.nickname10), R.drawable.avatar10, getString(R.string.sentence10), "1998/11/11"))
        data?.add(Chat(getString(R.string.nickname9), R.drawable.avatar9, getString(R.string.sentence9), "2000/04/16"))
        data?.add(Chat(getString(R.string.nickname10), R.drawable.avatar10, getString(R.string.sentence10), "1998/11/11"))
        listView?.adapter = ChatAdapter(data, context)
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