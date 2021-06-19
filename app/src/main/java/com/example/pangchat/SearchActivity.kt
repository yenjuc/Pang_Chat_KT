package com.example.pangchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pangchat.fragment.SearchFragment
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.FriendWebSocketClient

class SearchActivity : FragmentActivity() {

    private lateinit var friendWebSocketClient: FriendWebSocketClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);

        setContentView(R.layout.activity_search)

        if (intent.getStringExtra("search") == "friend" || intent.getStringExtra("search") == "user") {
            val searchFragment: Fragment = SearchFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment, searchFragment).commit()
        }




    }
}