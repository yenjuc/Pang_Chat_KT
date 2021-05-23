package com.example.pangchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.contact.Contact
import com.example.pangchat.contact.ContactAdapter
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.contact.ContactInfo
import com.example.pangchat.fragment.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */

data class ContactInfoResult(
        val success: ArrayList<String>? = null,
        val error: Int? = null
)


class ContactsFragment : Fragment() {
    private var recyclerView: RecyclerView? = null

    private val _contactResult = MutableLiveData<ContactInfoResult>()
    val contactResult: LiveData<ContactInfoResult> = _contactResult

    private lateinit var contacts:LinkedList<Contact?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.contacts_recyclerview)

        contacts = LinkedList<Contact?>()

        recyclerView?.adapter = ContactAdapter(contacts)

        lifecycleScope.launch {

            // 从Mainactivity的Intent中获取userId，作为入参传入网络请求
            activity?.intent?.getStringExtra("userId")?.let { getContactInfo(userId = it) }
            contacts.clear()
            contacts.addAll(_contactResult.value?.success?.map { Contact(it, R.drawable.avatar1) }!!)
            recyclerView?.adapter?.notifyDataSetChanged()
        }


        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_contacts, container, false)
    }

    suspend fun getContactInfo(userId: String) {
        val contactDataSource = ContactDataSource()

            val result: Result<ContactInfo>

            withContext(Dispatchers.IO) {
                result = contactDataSource.getContactInfo(userId)
            }

            if (result is Result.Success) {
                _contactResult.value = ContactInfoResult(success = result.data.friendsName)
            } else {
                _contactResult.value = ContactInfoResult(error = R.string.login_failed)
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