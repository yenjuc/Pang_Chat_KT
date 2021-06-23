package com.example.pangchat.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.MainActivity
import com.example.pangchat.R
import com.example.pangchat.afterTextChanged
import com.example.pangchat.contact.Contact
import com.example.pangchat.contact.ContactAdapter
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.contact.ContactInfo
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.UserDataSource
import com.example.pangchat.fragment.data.UserInfo
import com.example.pangchat.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class SearchFragment : Fragment() {
    private var backView: ImageView? = null
    private var editText: EditText ? = null
    private var buttonSearch: Button ? = null
    var recyclerView: RecyclerView ? = null

    var contacts = LinkedList<Contact?>()

    // var friendIds: ArrayList<String> ? = null
    // var friendNames: ArrayList<String> ? = null
    var friendsInfo = ArrayList<User>()

    var _userInfo = MutableLiveData<UserInfo>()
    val _contactInfo = MutableLiveData<ContactInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editText = getView()?.findViewById(R.id.search_content)
        backView = getView()?.findViewById(R.id.goback)
        buttonSearch = getView()?.findViewById(R.id.button_search)
        recyclerView = getView()?.findViewById(R.id.search_recyclerview)

        recyclerView?.adapter = ContactAdapter(activity, contacts)

        // friendNames = activity?.intent?.getStringArrayListExtra("friendNames")
        // friendIds = activity?.intent?.getStringArrayListExtra("friendIds")

        if (activity?.intent?.getStringExtra("search") == "friend") {
            // buttonSearch?.visibility = View.INVISIBLE;

            lifecycleScope.launch{
               getContactInfo()

               friendsInfo.clear()
               for (i in 0 until _contactInfo.value?.friendsInfo?.size!!) {
                   friendsInfo.add(_contactInfo.value!!.friendsInfo[i])
               }
            }

            editText?.afterTextChanged {
                val searchContent: String = editText?.text.toString()

                contacts.clear()
                fuzzyMatch(searchContent)

                if ((searchContent.isNotEmpty()) and (contacts.size != 0)) {
                    recyclerView?.adapter?.notifyDataSetChanged()

                    val linearLayoutManager = LinearLayoutManager(this.activity)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView?.layoutManager = linearLayoutManager
                }

            }

        }
        else if (activity?.intent?.getStringExtra("search") == "user") {
            buttonSearch?.setOnClickListener(View.OnClickListener {
                val searchContent: String = editText?.text.toString()

                lifecycleScope.launch {
                    getUserInfo(searchContent)

                    contacts.clear()

                    if (_userInfo.value?.success == true) {
                        contacts.add(
                            Contact(
                                _userInfo.value?.userId!!,
                                _userInfo.value?.username!!,
                                _userInfo.value?.nickname!!,
                                _userInfo.value?.avatar!!
                            )
                        )
                        recyclerView?.adapter?.notifyDataSetChanged()
                    }

                    val linearLayoutManager = LinearLayoutManager(activity)
                    linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                    recyclerView?.layoutManager = linearLayoutManager
                }
            })
        }

        backView?.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, MainActivity::class.java) }
            // intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            // intent.putExtra("friendIds", friendIds)
            // intent.putExtra("friendNames", friendNames)
            startActivity(intent)

            activity?.finish()
        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    fun fuzzyMatch(searchContent: String) {
        for (i in 0 until friendsInfo.size) {
            if (friendsInfo.get(i).getNickname().contains(searchContent, true) == true) {
                contacts.add(Contact(friendsInfo[i].getUserId(), friendsInfo[i].getUsername(),
                    friendsInfo[i].getNickname(), friendsInfo[i].getAvatar()))
            }
        }
    }

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

    suspend fun getUserInfo(username: String) {
        val userDataSource = UserDataSource()

        val result: Result<UserInfo>

        withContext(Dispatchers.IO) {
            result = userDataSource.getUserInfoByName(username)
        }

        if (result is Result.Success) {
            _userInfo.value = result.data
        } else {
            // TODO：抛出并解析异常
        }
    }




    companion object {
        fun newInstance(): SearchFragment {
            val fragment = SearchFragment()
            return fragment
        }
    }


}

//fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
//    this.addTextChangedListener(object : TextWatcher {
//        override fun afterTextChanged(editable: Editable?) {
//            afterTextChanged.invoke(editable.toString())
//        }
//
//        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
//
//        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
//    })
//}
//
