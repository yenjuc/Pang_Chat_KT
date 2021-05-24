package com.example.pangchat

import android.content.Context
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


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private var textView: TextView? = null
    private var imageView: ImageView? = null
    private var button: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        textView = getView()?.findViewById<TextView?>(R.id.username_text)
        textView?.setText(activity?.intent?.getStringExtra("username"))
        imageView = getView()?.findViewById<ImageView>(R.id.avatar_icon)
        imageView?.setImageResource(R.drawable.avatar1)
        button = getView()?.findViewById<Button>(R.id.button_modify)


        textView?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "修改昵称", Toast.LENGTH_LONG).show()

            // 进入修改昵称页面
            // 想法：统一写一个修改信息的fragment  activity
            // 根据修改内容的不同显示不同布局

            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, ModifyInfoActivity::class.java) }
            intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            intent.putExtra("username", activity?.intent?.getStringExtra("username"))
            intent.putExtra("modifyKey", "username")

            startActivity(intent)

            activity?.finish()

        })

        button?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "修改密码", Toast.LENGTH_LONG).show()

            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, ModifyInfoActivity::class.java) }
            intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            intent.putExtra("username", activity?.intent?.getStringExtra("username"))
            intent.putExtra("modifyKey", "password")

            startActivity(intent)

            activity?.finish()
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_settings, container, false)
    }



    companion object {
        fun newInstance(): SettingsFragment? {
            val fragment = SettingsFragment()
            return fragment
        }
    }
}