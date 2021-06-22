package com.example.pangchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.contact.*
import com.example.pangchat.fragment.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class ContactsFragment : Fragment() {
    private lateinit var mContext: FragmentActivity
    private lateinit var newFriendRecyclerView: RecyclerView
    private lateinit var currFriendRecyclerView: RecyclerView


    private var _contactInfo = MutableLiveData<ContactInfo>()

    lateinit var contacts:LinkedList<Contact?>
    lateinit var requests:LinkedList<Contact?>

    // private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        currFriendRecyclerView = view.findViewById(R.id.contacts_recyclerview)
        newFriendRecyclerView = view.findViewById(R.id.newfriends_recyclerview)

        contacts = LinkedList<Contact?>()
        requests = LinkedList<Contact?>()

        currFriendRecyclerView.adapter = ContactAdapter(activity, contacts)
        newFriendRecyclerView.adapter = FriendRequestAdapter(activity, requests)

        lifecycleScope.launch {

            // 获取现有好友列表
            getContactInfo()

            contacts.clear()
            for (index in 0 until _contactInfo.value?.friendsInfo?.size!!) {
                contacts.add(Contact(_contactInfo.value?.friendsInfo!![index].getUserId(),
                    _contactInfo.value?.friendsInfo!![index].getUsername(),
                    _contactInfo.value?.friendsInfo!![index].getNickname(),
                    R.drawable.avatar1 ))
            }
            currFriendRecyclerView.adapter?.notifyDataSetChanged()

            requests.clear()
            for (index in 0 until _contactInfo.value?.newfriendsInfo?.size!!) {
                requests.add(Contact(
                    _contactInfo.value?.newfriendsInfo!![index].getUserId(),
                    _contactInfo.value?.newfriendsInfo!![index].getUsername(),
                    _contactInfo.value?.newfriendsInfo!![index].getNickname(),
                    R.drawable.avatar1))
            }
            newFriendRecyclerView.adapter?.notifyDataSetChanged()
        }


        val _linearLayoutManager = LinearLayoutManager(this.activity)
        _linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        newFriendRecyclerView.layoutManager = _linearLayoutManager

        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        currFriendRecyclerView.layoutManager = linearLayoutManager


    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_contacts, container, false)
    }

    // 调用网络请求函数
    suspend fun getContactInfo() {
        val contactDataSource = ContactDataSource()
        val result: Result<ContactInfo>

        withContext(Dispatchers.IO) {
            result = contactDataSource.getContactInfo()
        }

        if (result is Result.Success) {
            _contactInfo.value = result.data
        } else {
            // TODO：抛出并解析异常
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ContactsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): ContactsFragment? {
            return ContactsFragment()
        }
    }
}