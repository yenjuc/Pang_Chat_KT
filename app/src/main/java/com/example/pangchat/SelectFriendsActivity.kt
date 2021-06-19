package com.example.pangchat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.github.kittinunf.fuel.core.FuelManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView

class SelectFriendsActivity : FragmentActivity() {
    // @BindView(R.id.bottomNavigationView)
    var recyclerView: RecyclerView? = null
    var selectedIds =  ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FuelManager.instance.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        val userId = intent.getStringExtra("userId")
        intent.putExtra("selectedIds", selectedIds)

        setContentView(R.layout.activity_select_friends)

        val selectFriendsFragment: Fragment = SelectFriendsFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment, selectFriendsFragment).commit()


    }
}