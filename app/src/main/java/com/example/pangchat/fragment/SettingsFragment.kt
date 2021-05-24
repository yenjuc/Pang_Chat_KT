package com.example.pangchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        textView = getView()?.findViewById<TextView?>(R.id.username_text)
        textView?.setText(activity?.intent?.getStringExtra("username"))
        imageView?.setImageResource(R.drawable.avatar1)

        val context: Context? = activity

        textView?.setOnClickListener(View.OnClickListener{
            Toast.makeText(activity, "修改昵称", Toast.LENGTH_LONG).show()

            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, ModifyInfoActivity::class.java) }
            intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            intent.putExtra("username", activity?.intent?.getStringExtra("username"))

            startActivity(intent)

            activity?.finish()

            // TODO: 进入修改昵称页面
            // 想法：统一写一个修改信息的fragment  activity
            // 根据修改内容的不同显示不同布局
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