package com.example.pangchat.fragment

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.MainActivity
import com.example.pangchat.R
import com.example.pangchat.fragment.data.ModifyNicknameResult
import com.example.pangchat.fragment.data.ModifyPasswordResult
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.fragment.data.SettingsDataSource
import com.example.pangchat.websocketClient.webSocketClient
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
            Toast.makeText(activity, "修改中", Toast.LENGTH_LONG).show()

            lifecycleScope.launch(){
                var modifyValue : String ? = null

                if (modifyKey == "nickname") {
                    modifyValue = editText?.getText().toString()
                    modifyNickname(modifyValue)
                }
                else if (modifyKey == "password") {
                    val oldPassword : String = editText?.getText().toString()
                    modifyValue = editText_1?.getText().toString()
                    val modifyValueConfirm = editText_2?.getText().toString()
                    if (modifyValue != modifyValueConfirm) {
                        Toast.makeText(activity, "新密码两次输入不一致！", Toast.LENGTH_LONG).show()
                    }
                    else
                    {
                        modifyPassword(oldPassword, modifyValue)
                    }
                }


                val intent = Intent()
                activity?.let { it1 -> intent.setClass(it1, MainActivity::class.java) }
                // intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
                intent.putExtra(modifyKey, modifyValue)

                startActivity(intent)

                activity?.finish()
            }
        })

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_modify_info, container, false)
    }


    suspend fun modifyNickname(newNickname : String) {
        val settingsDataSource = SettingsDataSource()

        val result: Result<ModifyNicknameResult>

        withContext(Dispatchers.IO) {
            result = settingsDataSource.modifyNickname(newNickname)
        }

        if (result is Result.Success && result.data.success) {
            Toast.makeText(activity, "修改昵称成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "修改昵称失败", Toast.LENGTH_SHORT).show()
        }
    }

    suspend fun modifyPassword(oldPassword: String, newPassword : String) {
        val settingsDataSource = SettingsDataSource()

        val result: Result<ModifyPasswordResult>

        withContext(Dispatchers.IO) {
            result = settingsDataSource.modifyPassword(oldPassword, newPassword)
        }

        if (result is Result.Success && result.data.success) {
            webSocketClient.password = newPassword
            Toast.makeText(activity, "修改密码成功", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(activity, "修改密码失败", Toast.LENGTH_SHORT).show()
        }
    }



    companion object {
        fun newInstance(): ModifyInfoFragment {
            val fragment = ModifyInfoFragment()
            return fragment
        }
    }

}