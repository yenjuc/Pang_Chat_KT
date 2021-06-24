package com.example.pangchat

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.contact.ContactInfo
import com.example.pangchat.fragment.*
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : FragmentActivity() {
    // @BindView(R.id.bottomNavigationView)
    // var topNavigationView: NavigationView? = null
    var bottomNavigationView: BottomNavigationView? =  null
    var searchView: ImageView? = null
    var menuView: ImageView? = null
    // var userId: String? = null
    // var username: String? = null
    var _contactInfo = MutableLiveData<ContactInfo>()
    var friendIds = ArrayList<String>()
    var friendNames = ArrayList<String>()

    val chatsFragment: Fragment = ChatsFragment()
    val contactsFragment: Fragment = ContactsFragment()
    val discoverFragment: Fragment = DiscoverFragment()
    val settingsFragment: Fragment = SettingsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);


        // val intent = intent
        // userId = intent.getStringExtra("userId")
        // username = intent.getStringExtra("username")

        setContentView(R.layout.activity_main)
        // ButterKnife.bind(this)

        if (intent.getStringExtra("fragment") == "contact") {
            setCurrentFragment(contactsFragment)
        }
        else if(intent.getStringExtra("fragment")=="discover"){
            setCurrentFragment(discoverFragment)
        }
        else{
            setCurrentFragment(chatsFragment) // 初始的Fragment为chatsFragment
        }

        MainScope().launch {
            getFriendsInfo()
            // friendIds = _contactInfo.value?.friendsId

            friendNames.clear()
            friendIds.clear()

            if (_contactInfo.value?.success == true) {
                for (index in 0 until (_contactInfo.value?.friendsInfo?.size!!)) {
                    _contactInfo.value?.friendsInfo!![index].let { friendNames.add(it.getUsername()) }
                    _contactInfo.value?.friendsInfo!![index].let { friendIds.add(it.getUserId()) }
                }
            }

            intent.putExtra("friendIds", friendIds)
            intent.putExtra("friendNames", friendNames)
        }


        searchView = findViewById<ImageView>(R.id.search)

        searchView?.setOnClickListener(View.OnClickListener {

            Toast.makeText(this, "搜索现有联系人", Toast.LENGTH_LONG).show()

            // 表示这个页面是搜索现有的联系人
            intent.putExtra("search", "friend")
            // intent.putExtra("userId", userId)

            intent.setClass(this@MainActivity, SearchActivity::class.java)

            startActivity(intent)
            this.finish()
        })

        menuView = findViewById<ImageView>(R.id.menu)

        menuView?.setOnClickListener(View.OnClickListener {
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

    override fun onResume() {
        super.onResume()
        webSocketClient.context = this
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
                    intent.putStringArrayListExtra("members", ArrayList<String>())
                    intent.setClass(this@MainActivity, SelectFriendsActivity::class.java)
                    // intent.putExtra("userId", userId)

                    startActivity(intent)
                    this.finish()
                    return@OnMenuItemClickListener true
                }
                R.id.newfriend -> {
                    Toast.makeText(this, "新建好友", Toast.LENGTH_LONG).show()

                    // 表示这个页面是搜索所有的用户
                    intent.putExtra("search", "user")
                    // intent.putExtra("userId", userId)
                    intent.setClass(this@MainActivity, SearchActivity::class.java)

                    startActivity(intent)
                    this.finish()
                    return@OnMenuItemClickListener true
                }
            }
            false
        })

        popupMenu.show()
    }

    suspend fun getFriendsInfo() {
        val contactDataSource = ContactDataSource()

        val result: Result<ContactInfo>

        withContext(Dispatchers.IO) {
            result = contactDataSource.getContactInfo()
        }

        if (result is Result.Success) {
            _contactInfo.value = result.data
        } else {
            // TODO：抛出并解析异常
        }
    }

}