package com.example.pangchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pangchat.fragment.ModifyInfoFragment
import com.example.pangchat.utils.CookiedFuel

class ModifyInfoActivity : FragmentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        // val userId = intent.getStringExtra("userId")
        val username = intent.getStringExtra("username")
        val modifyKey = intent.getStringExtra("modifyKey")

        setContentView(R.layout.activity_modify_info)
        // ButterKnife.bind(this)

        val modifyInfoFragment: Fragment = ModifyInfoFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment, modifyInfoFragment).commit()

    }


}