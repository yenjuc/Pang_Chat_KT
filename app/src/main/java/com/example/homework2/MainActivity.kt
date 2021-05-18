package com.example.homework2

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    // @BindView(R.id.bottomNavigationView)
    var bottomNavigationView: BottomNavigationView? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // ButterKnife.bind(this)

        // 修改下面的代码，添加向 settingsFragment的跳转逻辑
        // TODO
        val chatsFragment: Fragment = ChatsFragment()
        val contactsFragment: Fragment = ContactsFragment()
        val discoverFragment: Fragment = DiscoverFragment()
        val settingsFragment: Fragment = SettingsFragment()
        setCurrentFragment(chatsFragment) // 初始的Fragment为chatsFragment


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