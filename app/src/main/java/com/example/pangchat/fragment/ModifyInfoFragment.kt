package com.example.pangchat.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
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
import com.example.pangchat.fragment.data.ModifyPasswordResult
import com.example.pangchat.fragment.data.ModifyUsernameResult
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.SettingsDataSource
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ModifyInfoFragment : Fragment() {
    private var editText : EditText ? = null
    private var editText_1 : EditText ? = null
    private var editText_2 : EditText ? = null
    private var button : Button ? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        editText = getView()?.findViewById<EditText>(R.id.newUsername)
        editText_1 = getView()?.findViewById<EditText>(R.id.newPassword)
        editText_2 = getView()?.findViewById<EditText>(R.id.newPasswordAgain)

        button = getView()?.findViewById<Button>(R.id.button_save)
        val modifyKey : String ? = activity?.intent?.getStringExtra("modifyKey")

        if (modifyKey == "password") {
            editText?.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText?.hint = "input old password"
            editText_1?.visibility = View.VISIBLE
            editText_2?.visibility = View.VISIBLE
        }

        button?.setOnClickListener(View.OnClickListener{
            Toast.makeText(activity, "提交修改", Toast.LENGTH_LONG).show()

            lifecycleScope.launch(){
                var modifyValue : String ? = null

                if (modifyKey == "username") {
                    modifyValue = editText?.getText().toString()
                    activity?.intent?.getStringExtra("userId")?.let { it1 -> modifyUsername(it1, modifyValue!!) }
                    // TODO： 异常处理
                }
                else if (modifyKey == "password") {
                    // TODO: 检查两次输入的新密码是否相同

                    val oldPassword : String = editText?.getText().toString()
                    modifyValue = editText_1?.getText().toString()
                    activity?.intent?.getStringExtra("userId")?.let { it1 -> modifyPassword(it1, oldPassword, modifyValue) }
                    // TODO: 异常处理
                }


                val intent = Intent()
                activity?.let { it1 -> intent.setClass(it1, MainActivity::class.java) }
                intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
                intent.putExtra(modifyKey, modifyValue)

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

    suspend fun modifyPassword(userId : String, oldPassword: String, newPassword : String) {
        val settingsDataSource = SettingsDataSource()

        val result: Result<ModifyPasswordResult>

        withContext(Dispatchers.IO) {
            result = settingsDataSource.modifyPassword(userId, oldPassword, newPassword)
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