package com.example.pangchat

import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pangchat.fragment.*
import com.github.kittinunf.fuel.core.FuelManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class ModifyInfoActivity : FragmentActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FuelManager.instance.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        val userId = intent.getStringExtra("userId")
        val username = intent.getStringExtra("username")
        val modifyKey = intent.getStringExtra("modifyKey")

        setContentView(R.layout.activity_modify_info)
        // ButterKnife.bind(this)

        val modifyInfoFragment: Fragment = ModifyInfoFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment, modifyInfoFragment).commit()

    }


}