package com.example.pangchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R
import com.example.pangchat.contact.Contact
import com.example.pangchat.contact.ContactAdapter
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [ContactsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ContactsFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.contacts_recylerview)

        // 添加数据，为recyclerView绑定Adapter、LayoutManager
        // 添加数据的样例代码如下:
        // LinkedList<Contact> contacts = new LinkedList<>();
        // contacts.add(new Contact(getString(R.string.nickname1), R.drawable.avatar1));
        // contacts.add(new Contact(getString(R.string.nickname2), R.drawable.avatar2));
        // TODO
        val contacts = LinkedList<Contact?>()
        contacts.add(Contact(getString(R.string.nickname1), R.drawable.avatar1))
        contacts.add(Contact(getString(R.string.nickname2), R.drawable.avatar2))
        contacts.add(Contact(getString(R.string.nickname3), R.drawable.avatar3))
        contacts.add(Contact(getString(R.string.nickname4), R.drawable.avatar4))
        contacts.add(Contact(getString(R.string.nickname5), R.drawable.avatar5))
        contacts.add(Contact(getString(R.string.nickname6), R.drawable.avatar6))
        contacts.add(Contact(getString(R.string.nickname7), R.drawable.avatar7))
        contacts.add(Contact(getString(R.string.nickname8), R.drawable.avatar8))
        contacts.add(Contact(getString(R.string.nickname9), R.drawable.avatar9))
        contacts.add(Contact(getString(R.string.nickname10), R.drawable.avatar10))
        recyclerView?.adapter = ContactAdapter(contacts)
        val linearLayoutManager = LinearLayoutManager(this.activity)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recyclerView?.layoutManager = linearLayoutManager
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_contacts, container, false)
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