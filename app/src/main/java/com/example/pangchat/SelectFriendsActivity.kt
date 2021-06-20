package com.example.pangchat

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.pangchat.utils.CookiedFuel

class SelectFriendsActivity : FragmentActivity() {
    // @BindView(R.id.bottomNavigationView)
    // var recyclerView: RecyclerView? = null
    var selectedIds =  ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        // val userId = intent.getStringExtra("userId")
        intent.putExtra("selectedIds", selectedIds)

        setContentView(R.layout.activity_select_friends)

        val selectFriendsFragment: Fragment = SelectFriendsFragment()

        supportFragmentManager.beginTransaction().replace(R.id.fragment, selectFriendsFragment).commit()


    }
}