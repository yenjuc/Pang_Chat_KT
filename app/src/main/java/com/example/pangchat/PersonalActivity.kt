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
import com.example.pangchat.contact.IsFriendResult
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
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

    lateinit var userId: String
    lateinit var username: String
    var avatar: Int = 0
    // var friendNames: ArrayList<String>? = null

    private var _addFriendResult = MutableLiveData<AddFriendResult>()
    private var _isFriendResult = MutableLiveData<IsFriendResult>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);
        webSocketClient.context = this

        val intent = intent
        // myUserId = intent.getStringExtra("myUserId")
        userId = intent.getStringExtra("userId").toString()
        username = intent.getStringExtra("username").toString()
        avatar = intent.getIntExtra("avatar", 0)

        setContentView(R.layout.activity_personal)

        backView = findViewById<ImageView>(R.id.goback)
        moreView = findViewById<ImageView>(R.id.more)
        avatarView = findViewById<ImageView>(R.id.avatar_icon)
        usernameView = findViewById<TextView>(R.id.nickname_text)
        userIdView = findViewById<TextView>(R.id.userid_text)

        messageLayout = findViewById<LinearLayout>(R.id.layout_message)
        var textView = findViewById<TextView>(R.id.newfriend)

        val context: Context = this

        // friendNames = intent.getStringArrayListExtra("friendNames")

        MainScope().launch {
            isFriend(userId)

            if (webSocketClient.username == username) {
                messageLayout?.visibility = View.INVISIBLE
            }
            else if (_isFriendResult.value?.isFriend == true) {
                messageLayout?.setOnClickListener(View.OnClickListener {
                    Toast.makeText(context, "发消息", Toast.LENGTH_LONG).show()
                })
            }
            else {
                textView.text = "加好友"
                messageLayout?.setOnClickListener(View.OnClickListener {
                    Toast.makeText(context, "加好友", Toast.LENGTH_LONG).show()
                    // TODO: 加好友的操作

                    MainScope().launch {

                        username.let { it1 -> sendFriendRequest(it1) }

                        if (_addFriendResult.value?.success == true) {
                            Toast.makeText(context, "成功发送好友申请", Toast.LENGTH_LONG).show()
                        }
                    }
                    finish()

                })
            }

        }



        backView?.setOnClickListener(View.OnClickListener {
            val intent = Intent()
            intent.putExtra("userId", userId)
            this.finish()
        })

        usernameView?.text = username
        userIdView?.text = userId
        avatarView?.setImageResource(avatar)
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

    suspend fun isFriend(friendId: String) {
        val contactDataSource = ContactDataSource()

        val result: Result<IsFriendResult>

        withContext(Dispatchers.IO) {
            result = contactDataSource.isFriend(friendId)
        }

        if (result is Result.Success) {
            _isFriendResult.value = result.data
        } else {
            print("ex")
            // TODO：抛出并解析异常
        }
    }

}