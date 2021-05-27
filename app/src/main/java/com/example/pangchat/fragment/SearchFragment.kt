package com.example.pangchat.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.SearchActivity
import com.example.pangchat.R
import com.example.pangchat.SettingsFragment
import com.example.pangchat.afterTextChanged
import com.example.pangchat.contact.Contact
import com.example.pangchat.contact.ContactAdapter
import java.util.*
import kotlin.collections.ArrayList


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */


class SearchFragment : Fragment() {

    private var editText: EditText ? = null
    private var buttonCancel: Button ? = null
    var recyclerView: RecyclerView ? = null
    var contacts = LinkedList<Contact?>()

    var friendIds: ArrayList<String> ? = null
    var friendNames: ArrayList<String> ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editText = getView()?.findViewById(R.id.search_content)
        buttonCancel = getView()?.findViewById(R.id.search_cancel)
        recyclerView = getView()?.findViewById(R.id.search_recyclerview)

        recyclerView?.adapter = ContactAdapter(activity, contacts)

        friendNames = activity?.intent?.getStringArrayListExtra("friendNames")
        friendIds = activity?.intent?.getStringArrayListExtra("friendIds")

        editText?.afterTextChanged {
            val searchContent : String = editText?.text.toString()

            contacts.clear()
            fuzzyMatch(searchContent)

            if ((searchContent.isNotEmpty()) and (contacts.size != 0)){
                recyclerView?.adapter?.notifyDataSetChanged()

                val linearLayoutManager = LinearLayoutManager(this.activity)
                linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
                recyclerView?.layoutManager = linearLayoutManager
            }

        }



        buttonCancel?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "返回", Toast.LENGTH_LONG).show()

            activity?.finish()
        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    fun fuzzyMatch(searchContent: String) {
        for (i in 0 until friendIds?.size!!) {
            if (friendNames?.get(i)?.contains(searchContent, true) == true) {
                contacts.add(Contact(friendIds!![i], friendNames!![i], R.drawable.avatar1))
            }
        }
    }



    companion object {
        fun newInstance(): SearchFragment? {
            val fragment = SearchFragment()
            return fragment
        }
    }


}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}

