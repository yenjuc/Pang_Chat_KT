package com.example.pangchat

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.pangchat.chat.Chat
import com.example.pangchat.chat.data.ChatInfo
import com.example.pangchat.chat.data.ChatRequest
import com.example.pangchat.chat.data.ChatResult
import com.example.pangchat.contact.AddFriendResult
import com.example.pangchat.contact.ContactDataSource
import com.example.pangchat.contact.IsFriendResult
import com.example.pangchat.fragment.data.Result
import com.example.pangchat.utils.CookiedFuel
import com.example.pangchat.websocketClient.webSocketClient
import com.github.kittinunf.fuel.coroutines.awaitByteArray
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PersonalActivity : FragmentActivity() {

    var backView: ImageView ? = null
    // var moreView: ImageView ? = null
    var avatarView: ImageView ? = null
    var usernameView: TextView ? = null
    var userIdView: TextView ? = null
    var messageLayout : LinearLayout ? = null
    var deleteLayout: LinearLayout? = null

    lateinit var userId: String
    lateinit var username: String
    lateinit var avatar: String


    var _addFriendResult = MutableLiveData<AddFriendResult>()
    var _isFriendResult = MutableLiveData<IsFriendResult>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CookiedFuel.basePath = resources.getString(R.string.BACKEND_URL);
        webSocketClient.context = this

        val intent = intent
        userId = intent.getStringExtra("userId").toString()
        username = intent.getStringExtra("username").toString()
        avatar = intent.getStringExtra("avatar").toString()

        setContentView(R.layout.activity_personal)

        backView = findViewById<ImageView>(R.id.goback)
        // moreView = findViewById<ImageView>(R.id.more)
        avatarView = findViewById<ImageView>(R.id.avatar_icon)
        usernameView = findViewById<TextView>(R.id.nickname_text)
        userIdView = findViewById<TextView>(R.id.userid_text)

        messageLayout = findViewById<LinearLayout>(R.id.layout_message)
        deleteLayout = findViewById<LinearLayout>(R.id.layout_delete)

        var textView = findViewById<TextView>(R.id.newfriend)

        val context: Context = this

        // friendNames = intent.getStringArrayListExtra("friendNames")

        lifecycleScope.launch{
            // ????????????????????????
            if(!webSocketClient.urlToBitmap.keys.contains(avatar)){
                val bit: Bitmap;
                withContext(Dispatchers.IO) {
                    val result = CookiedFuel.get(avatar).awaitByteArray();
                    bit = BitmapFactory.decodeByteArray(result, 0, result.size)
                    webSocketClient.urlToBitmap.put(avatar, bit)
                }
            }
            avatarView?.setImageBitmap(webSocketClient.urlToBitmap[avatar]) // ????????????IO??????
        }


        MainScope().launch {

            isFriend(userId)

            if (webSocketClient.username == username) {
                messageLayout?.visibility = View.GONE
                deleteLayout?.visibility = View.GONE
            }
            else if (_isFriendResult.value?.isFriend == true) {
                messageLayout?.setOnClickListener(View.OnClickListener {
                    Toast.makeText(context, "?????????", Toast.LENGTH_LONG).show()
                })
                deleteLayout?.setOnClickListener(View.OnClickListener {
                    // ???????????????????????????
                    showNormalDialog()
                })
                messageLayout?.setOnClickListener {
                    lifecycleScope.launch {
                        var chat: Chat? = newChat(arrayListOf(userId))
                        if (chat != null) {
                            Log.d("click chatid: ", chat.getId())
                            val intent = Intent(this@PersonalActivity, ChatActivity::class.java)
                            intent.putExtra("chatId", chat.getId())
                            try {
                                startActivity(intent)
                            } catch (ActivityNotFoundException: Exception) {
                                Log.d("ImplicitIntents", "Can't handle this!")
                            }
                        }
                    }
                }
                messageLayout?.setOnClickListener(View.OnClickListener {

                        lifecycleScope.launch {
                            var chat: Chat? = newChat(arrayListOf(userId))
                            if (chat != null) {
                                Log.d("click chatid: ", chat.getId())
                                val intent = Intent(this@PersonalActivity, ChatActivity::class.java)
                                intent.putExtra("chatId", chat.getId())
                                try {
                                    startActivity(intent)
                                } catch (ActivityNotFoundException: Exception) {
                                    Log.d("ImplicitIntents", "Can't handle this!")
                                }
                            }
                        }
                })
                deleteLayout?.setOnClickListener(View.OnClickListener {
                    // ???????????????????????????
                    showNormalDialog()
                })
            }
            else {
                textView.text = "?????????"
                deleteLayout?.visibility = View.GONE
                messageLayout?.setOnClickListener(View.OnClickListener {
                    Toast.makeText(context, "?????????", Toast.LENGTH_LONG).show()
                    MainScope().launch {

                        username.let { it1 -> sendFriendRequest(it1) }

                        if (_addFriendResult.value?.success == true) {
                            Toast.makeText(context, "????????????????????????", Toast.LENGTH_LONG).show()
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
        // TODO: ????????????????????????????????????????????????




    }

    override fun onResume() {
        super.onResume()
        webSocketClient.context = this
    }

    private fun showNormalDialog(){
        val normalDialog = AlertDialog.Builder(this);
        normalDialog.setIcon(R.drawable.avatar1);
        // normalDialog.setTitle("??????????????????Dialog")
        normalDialog.setMessage("?????????????????????????");
        normalDialog.setPositiveButton("??????",
             DialogInterface.OnClickListener() {
                 dialog: DialogInterface, which: Int ->
                 // ????????????????????????
                 MainScope().launch {
                     deleteFriend(friendName = username)
                     finish()
                     val intent = Intent(this@PersonalActivity, MainActivity::class.java)
                     intent.putExtra("fragment", "contact")

                     try {
                         startActivity(intent)
                     } catch (ActivityNotFoundException: Exception) {
                         Log.d("ImplicitIntents", "Can't handle this!")
                     }
                 }
                 dialog.dismiss()
            })
        normalDialog.setNegativeButton("??????",
             DialogInterface.OnClickListener() {
                dialog: DialogInterface, which: Int ->
                 dialog.dismiss()
            });
        // ??????
        normalDialog.show();
    }



    private fun activityFinish(){
        this.finish()
    }

    // ????????????????????????
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
            // TODO????????????????????????
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
            // TODO????????????????????????
        }
    }

    suspend fun deleteFriend(friendName: String) {
        val contactDataSource = ContactDataSource()

        val result: Result<AddFriendResult>

        withContext(Dispatchers.IO) {
            result = contactDataSource.deleteFriend(friendName)
        }

        if (result is Result.Success) {

        } else {
            print("ex")
            // TODO????????????????????????
        }
    }

    suspend fun newChat(members: ArrayList<String>): Chat?{
        val chatRequest = ChatRequest()
        val result: ChatResult<ChatInfo>

        withContext(Dispatchers.IO) {
            result = chatRequest.newChat(webSocketClient.userId!!, members)
        }

        if (result is ChatResult.Success) {
            Log.d("chat", "new");
            return result.data.chat
        } else {
            // TODO????????????????????????
            return null
        }
    }

}