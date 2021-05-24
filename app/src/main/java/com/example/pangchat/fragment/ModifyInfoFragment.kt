package com.example.pangchat.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.MainActivity
import com.example.pangchat.ModifyInfoActivity
import com.example.pangchat.R
import com.example.pangchat.SettingsFragment
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.contact.ContactInfo
import com.example.pangchat.fragment.data.ModifyUsernameResult
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.SettingsDataSource
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModifyInfoFragment : Fragment() {
    private var editText : EditText ? = null
    private var button : Button ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        editText = getView()?.findViewById<EditText>(R.id.newUsername)

        button = getView()?.findViewById<Button>(R.id.button_save)


        val context: Context? = activity

        button?.setOnClickListener(View.OnClickListener{
            Toast.makeText(activity, "提交修改", Toast.LENGTH_LONG).show()

            lifecycleScope.launch(){
                val newUsername : String = editText?.getText().toString()
                activity?.intent?.getStringExtra("userId")?.let { it1 -> modifyUsername(it1, newUsername) }

                // TODO： 异常处理
                val intent = Intent()
                activity?.let { it1 -> intent.setClass(it1, MainActivity::class.java) }
                intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
                intent.putExtra("username", newUsername)

                startActivity(intent)

                activity?.finish()
            }
        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_modify_info, container, false)
    }


    suspend fun modifyUsername(userId : String, newUsername : String) {
        val settingsDataSource = SettingsDataSource()

        val result: Result<ModifyUsernameResult>

        withContext(Dispatchers.IO) {
            result = settingsDataSource.modifyUsername(userId, newUsername)
        }

        if (result is Result.Success) {

        } else {
            // TODO：抛出并解析异常
        }
    }

    companion object {
        fun newInstance(): ModifyInfoFragment? {
            val fragment = ModifyInfoFragment()
            return fragment
        }
    }

}