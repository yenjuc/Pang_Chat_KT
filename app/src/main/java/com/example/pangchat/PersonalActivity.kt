package com.example.pangchat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import com.example.pangchat.contact.AddFriendResult
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.utils.CookiedFuel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    private var _addFriendResult = MutableLiveData<AddFriendResult>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);

        val intent = intent
        // myUserId = intent.getStringExtra("myUserId")
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
                // Toast.makeText(this, "加好友", Toast.LENGTH_LONG).show()
                // TODO: 加好友的操作
                val context: Context = this

                MainScope().launch {

                    username?.let { it1 -> sendFriendRequest(it1) }

                    if (_addFriendResult.value?.success == true) {
                        Toast.makeText(context, "成功发送好友申请", Toast.LENGTH_LONG).show()
                    }
                }
                finish()

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

    // 调用网络请求函数
    suspend fun sendFriendRequest(friendName: String) {
        val contactDataSource = ContactDataSource()

        val result: Result<AddFriendResult>

        withContext(Dispatchers.IO) {
            result = contactDataSource.addNewFriend(friendName)
        }

        if (result is Result.Success) {
            _addFriendResult.value = result.data
        } else {
            print("ex")
            // TODO：抛出并解析异常
        }
    }
}