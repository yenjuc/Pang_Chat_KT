package com.example.pangchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pangchat.fragment.SearchFragment
import com.github.kittinunf.fuel.core.FuelManager

class SearchActivity : FragmentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FuelManager.instance.basePath = resources.getString(R.string.BACKEND_URL);

        setContentView(R.layout.activity_search)

        val searchFragment: Fragment = SearchFragment()
        supportFragmentManager.beginTransaction().replace(R.id.fragment, searchFragment).commit()

    }
}