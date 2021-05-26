package com.example.pangchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.github.kittinunf.fuel.core.FuelManager

class PersonalActivity : FragmentActivity() {

    var backView: ImageView ? = null
    var moreView: ImageView ? = null

    var userId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FuelManager.instance.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        userId = intent.getStringExtra("userId")

        setContentView(R.layout.activity_personal)

        backView = findViewById<ImageView>(R.id.goback)
        moreView = findViewById<ImageView>(R.id.more)


        backView?.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.putExtra("userId", userId)
            this.finish()
        })

    }
}