package com.example.pangchat.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.pangchat.ModifyInfoActivity
import com.example.pangchat.R
import com.example.pangchat.SettingsFragment


/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {

    private var editText: EditText ? = null
    private var buttonCancel: Button ? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        editText = getView()?.findViewById(R.id.search_content)
        buttonCancel = getView()?.findViewById(R.id.search_cancel)

        // TODO: 根据输入框中的内容实时显示模糊匹配结果

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

    companion object {
        fun newInstance(): SearchFragment? {
            val fragment = SearchFragment()
            return fragment
        }
    }
}