package com.example.pangchat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.contact.*
import com.example.pangchat.fragment.data.Result
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class SelectFriendsFragment : Fragment() {
    private lateinit var mContext: FragmentActivity

    private var topNavigationView: NavigationView? = null
    private var recyclerView: RecyclerView? = null
    private var buttonFinish: Button? = null
    private var backView: ImageView? = null
    private val _contactInfo = MutableLiveData<ContactInfo>()

    private lateinit var contacts:LinkedList<Contact?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mContext = requireActivity();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.select_friends_recyclerview)

        topNavigationView = view.findViewById(R.id.topNavigationView)
        val headerView = topNavigationView?.getHeaderView(0)

        buttonFinish = headerView?.findViewById(R.id.button_finish)
        backView = headerView?.findViewById(R.id.goback)

        contacts = LinkedList<Contact?>()

        recyclerView?.adapter = SelectFriendsAdapter(contacts)

        lifecycleScope.launch {

            // 从Mainactivity的Intent中获取userId，作为入参传入网络请求
            activity?.intent?.getStringExtra("userId")?.let { getContactInfo(userId = it) }
            contacts.clear()
            contacts.addAll(_contactInfo.value?.friendsName?.map { Contact(it, R.drawable.avatar1) }!!)
            recyclerView?.adapter?.notifyDataSetChanged()
        }


        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager


        backView?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "返回", Toast.LENGTH_LONG).show()

            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, MainActivity::class.java) }
            intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            startActivity(intent)

            activity?.finish()
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_select_friends, container, false)
    }

    // 调用网络请求函数
    suspend fun getContactInfo(userId: String) {
        val contactDataSource = ContactDataSource()

        val result: Result<ContactInfo>

        withContext(Dispatchers.IO) {
            result = contactDataSource.getContactInfo(userId)
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
        fun newInstance(): SelectFriendsFragment? {
            return SelectFriendsFragment()
        }
    }
}