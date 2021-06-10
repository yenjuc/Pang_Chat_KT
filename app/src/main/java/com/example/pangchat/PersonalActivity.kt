package com.example.pangchat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.github.kittinunf.fuel.core.FuelManager
import org.w3c.dom.Text

class PersonalActivity : FragmentActivity() {

    var backView: ImageView ? = null
    var moreView: ImageView ? = null
    var avatarView: ImageView ? = null
    var usernameView: TextView ? = null
    var userIdView: TextView ? = null
    var messageLayout : LinearLayout ? = null

    var userId: String? = null
    var username: String? = null
    var myUserId: String? = null
    var avatar: Int? = null
    var friendIds: ArrayList<String>? = null
    var isFriend: Boolean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FuelManager.instance.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        myUserId = intent.getStringExtra("myUserId")
        userId = intent.getStringExtra("userId")
        username = intent.getStringExtra("username")
        avatar = intent.getIntExtra("avatar", 0)

        setContentView(R.layout.activity_personal)

        backView = findViewById<ImageView>(R.id.goback)
        moreView = findViewById<ImageView>(R.id.more)
        avatarView = findViewById<ImageView>(R.id.avatar_icon)
        usernameView = findViewById<TextView>(R.id.nickname_text)
        userIdView = findViewById<TextView>(R.id.userid_text)

        messageLayout = findViewById<LinearLayout>(R.id.layout_message)
        var textView = findViewById<TextView>(R.id.newfriend)

        friendIds = intent.getStringArrayListExtra("friendIds")
        if (myUserId == userId) {
            messageLayout?.visibility = View.INVISIBLE
        }
        else if (friendIds?.contains(userId) == true) {
            messageLayout?.setOnClickListener(View.OnClickListener {
                Toast.makeText(this, "发消息", Toast.LENGTH_LONG).show()
            })
        }
        else {
            textView.text = "加好友"
            messageLayout?.setOnClickListener(View.OnClickListener {
                Toast.makeText(this, "加好友", Toast.LENGTH_LONG).show()
                // TODO: 加好友的操作 
            })
        }


        backView?.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.putExtra("userId", userId)
            this.finish()
        })

        usernameView?.text = username
        userIdView?.text = userId
        avatarView?.setImageResource(avatar!!)
        // TODO: 后续可以通过网络请求设置更多信息




    }
}