package com.example.pangchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pangchat.fragment.SearchFragment
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient

class SearchActivity : FragmentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);
        webSocketClient.context = this

        setContentView(R.layout.activity_search)

        if (intent.getStringExtra("search") == "friend" || intent.getStringExtra("search") == "user") {
            val searchFragment: Fragment = SearchFragment()
            supportFragmentManager.beginTransaction().replace(R.id.fragment, searchFragment).commit()
        }




    }
}