package com.example.pangchat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.contact.Contact
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.contact.ContactInfo
import com.example.pangchat.contact.SelectFriendsAdapter
import com.example.pangchat.fragment.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class SelectFriendsFragment : Fragment() {
    private lateinit var mContext: FragmentActivity

    private var recyclerView: RecyclerView? = null
    private var buttonFinish: Button? = null
    private var backView: ImageView? = null
    private val _contactInfo = MutableLiveData<ContactInfo>()

    private lateinit var contacts:LinkedList<Contact?>
    private var members = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.select_friends_recyclerview)

        buttonFinish = getView()?.findViewById(R.id.button_finish)
        backView = getView()?.findViewById(R.id.goback)

        contacts = LinkedList<Contact?>()
        recyclerView?.adapter = SelectFriendsAdapter(activity, contacts)

        // val friendNames = activity?.intent?.getStringArrayListExtra("friendNames")
        // val friendIds = activity?.intent?.getStringArrayListExtra("friendIds")

        members = activity?.intent?.getStringArrayListExtra("members") as ArrayList<String>

        lifecycleScope.launch {
            getFriendsInfo()
            contacts.clear()

            for (index in 0 until (_contactInfo.value?.friendsInfo?.size!!)) {
                if (_contactInfo.value?.friendsInfo!![index].getUserId() in members == false) {
                    contacts.add(
                        Contact(
                            _contactInfo.value?.friendsInfo!![index].getUserId(),
                            _contactInfo.value?.friendsInfo!![index].getUsername(),
                            R.drawable.avatar1
                        )
                    )
                }

            }
            recyclerView?.adapter?.notifyDataSetChanged()
        }


//        lifecycleScope.launch {
//
//            // 从Mainactivity的Intent中获取userId，作为入参传入网络请求
//            val userId : String? = activity?.intent?.getStringExtra("userId")
//            if (userId != null) {
//                contacts.clear()
//                contacts.addAll(_contactInfo.value?.friendsName?.map { Contact(userId, it, R.drawable.avatar1) }!!)
//                recyclerView?.adapter?.notifyDataSetChanged()
//            }
//        }

        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager

        backView?.setOnClickListener(View.OnClickListener {

            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, MainActivity::class.java) }
            // intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            startActivity(intent)

            activity?.finish()
        })


        buttonFinish?.setOnClickListener(View.OnClickListener{
            val selectedNames = activity?.intent?.getStringArrayListExtra("selectedNames")
            // TODO: 发送建立群聊的网络请求
            activity?.finish()
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_select_friends, container, false)
    }

    suspend fun getFriendsInfo() {
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
//    // 调用网络请求函数
//    suspend fun getContactInfo(userId: String) {
//        val contactDataSource = ContactDataSource()
//
//        val result: Result<ContactInfo>
//
//        withContext(Dispatchers.IO) {
//            result = contactDataSource.getContactInfo(userId)
//        }
//
//        if (result is Result.Success) {
//            _contactInfo.value = result.data
//        } else {
//            // TODO：抛出并解析异常
//        }
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ContactsFragment.
         */
        // TODO: Rename and change types and number of parameters
        fun newInstance(): SelectFriendsFragment? {
            return SelectFriendsFragment()
        }
    }
}