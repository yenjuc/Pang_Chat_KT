package com.example.pangchat

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.view.menu.MenuPopupHelper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.contact.Contact
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.contact.ContactInfo
import com.example.pangchat.fragment.*
import com.example.pangchat.fragment.data.Result
import com.github.kittinunf.fuel.core.FuelManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : FragmentActivity() {
    // @BindView(R.id.bottomNavigationView)
    // var topNavigationView: NavigationView? = null
    var bottomNavigationView: BottomNavigationView? =  null
    var searchView: ImageView? = null
    var menuView: ImageView? = null
    var userId: String? = null
    var _contactInfo = MutableLiveData<ContactInfo>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FuelManager.instance.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        userId = intent.getStringExtra("userId")

        setContentView(R.layout.activity_main)
        // ButterKnife.bind(this)

        val chatsFragment: Fragment = ChatsFragment()
        val contactsFragment: Fragment = ContactsFragment()
        val discoverFragment: Fragment = DiscoverFragment()
        val settingsFragment: Fragment = SettingsFragment()
        setCurrentFragment(chatsFragment) // 初始的Fragment为chatsFragment

        var friendIds: ArrayList<String>? = null
        var friendNames: ArrayList<String>? = null

        MainScope().launch {
            userId?.let { getFriendsInfo(it) }
            friendIds = _contactInfo.value?.friendsId
            friendNames = _contactInfo.value?.friendsName
        }


        searchView = findViewById<ImageView>(R.id.search)

        searchView?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "进入搜索", Toast.LENGTH_LONG).show()

            val intent = Intent()
            // 表示这个页面是搜索现有的联系人
            intent.putExtra("search", "friend")
            intent.putExtra("userId", userId)
            intent.putExtra("friendIds", friendIds)
            intent.putExtra("friendNames", friendNames)
            intent.setClass(this@MainActivity, SearchActivity::class.java)

            startActivity(intent)
        })

        menuView = findViewById<ImageView>(R.id.menu)

        menuView?.setOnClickListener(View.OnClickListener {
            Toast.makeText(this, "显示菜单", Toast.LENGTH_LONG).show()

            showPopupMenu(this, menuView!!)
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

    @SuppressLint("RestrictedApi")
    private fun showPopupMenu(context: Context, view: View) {
        val popupMenu = PopupMenu(this, view)
        popupMenu.menuInflater.inflate(R.menu.add_nav_menu, popupMenu.menu)

//        val menuHelper = MenuPopupHelper(context, MenuBuilder(context), view)
//        menuHelper.setForceShowIcon(true);

        popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item: MenuItem? ->
            when (item?.itemId) {
                R.id.newgroup -> {
                    Toast.makeText(this, "发起群聊", Toast.LENGTH_LONG).show()

                    val intent = Intent()
                    intent.setClass(this@MainActivity, SelectFriendsActivity::class.java)
                    intent.putExtra("userId", userId)
                    startActivity(intent)
                    this.finish()
                    return@OnMenuItemClickListener true
                }
                R.id.newfriend -> {
                    Toast.makeText(this, "新建好友", Toast.LENGTH_LONG).show()

                    val intent = Intent()
                    // 表示这个页面是搜索所有的用户
                    intent.putExtra("search", "user")
                    intent.putExtra("userId", userId)
                    intent.setClass(this@MainActivity, SearchActivity::class.java)

                    startActivity(intent)
                    this.finish()
                    return@OnMenuItemClickListener true
                }
            }
            false
        })

//        try {
//            val field: Field = popupMenu.javaClass.getDeclaredField("mPopup")
//            field.isAccessible = true
//            val mHelper = field.get(popupMenu) as MenuPopupHelper
//            mHelper.setForceShowIcon(true)
//        } catch (e: IllegalAccessException) {
//            e.printStackTrace()
//        } catch (e: NoSuchFieldException) {
//            e.printStackTrace()
//        }


        popupMenu.show()
    }

    suspend fun getFriendsInfo(userId: String) {
        val contactDataSource = ContactDataSource()

        val result: Result<ContactInfo>

        withContext(Dispatchers.IO) {
            result = contactDataSource.getContactInfo(userId)
        }

        if (result is Result.Success) {
            _contactInfo.value = result.data
        } else {
            // TODO：抛出并解析异常
        }
    }

}