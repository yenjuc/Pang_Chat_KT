package com.example.pangchat

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pangchat.fragment.*
import com.github.kittinunf.fuel.core.FuelManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView


class MainActivity : FragmentActivity() {
    // @BindView(R.id.bottomNavigationView)
    var topNavigationView: NavigationView? = null
    var bottomNavigationView: BottomNavigationView? =  null
    var searchView: ImageView? = null
    var menuView: ImageView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FuelManager.instance.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        val userId = intent.getStringExtra("userId")

        setContentView(R.layout.activity_main)
        // ButterKnife.bind(this)

        // 修改下面的代码，添加向 settingsFragment的跳转逻辑
        // TODO
        val chatsFragment: Fragment = ChatsFragment()
        val contactsFragment: Fragment = ContactsFragment()
        val discoverFragment: Fragment = DiscoverFragment()
        val settingsFragment: Fragment = SettingsFragment()
        setCurrentFragment(chatsFragment) // 初始的Fragment为chatsFragment

        // FIXME: 暂时用一个 bottom navigation bottom 测试
        val messagesFragment: Fragment = MessagesFragment()

        topNavigationView = findViewById<NavigationView>(R.id.topNavigationView)

        val headerView : View? = topNavigationView?.getHeaderView(0)
        searchView = headerView?.findViewById<ImageView>(R.id.search)

        searchView?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "进入搜索", Toast.LENGTH_LONG).show()

            val intent = Intent()
            intent.setClass(this@MainActivity, SearchActivity::class.java)

            startActivity(intent)
        })

        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView?.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener setOnNavigationItemSelectedListener@{ item: MenuItem? ->
            when (item?.itemId) {
                R.id.chats -> {
                    setCurrentFragment(chatsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.contacts -> {
                    setCurrentFragment(contactsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.discover -> {
                    setCurrentFragment(discoverFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.settings -> {
                    setCurrentFragment(settingsFragment)
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.messages -> {
                    setCurrentFragment(messagesFragment)
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
        )
    }

    private fun setCurrentFragment(fragment: Fragment?) {
        if (fragment != null) {
            supportFragmentManager.beginTransaction().replace(R.id.flFragment, fragment).commit()
        }
    }


}

