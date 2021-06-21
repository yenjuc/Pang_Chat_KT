package com.example.pangchat

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.pangchat.websocketClient.webSocketClient


/**
 * A simple [Fragment] subclass.
 * Use the [SettingsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingsFragment : Fragment() {
    private var textView: TextView? = null
    private var imageView: ImageView? = null
    private var button: Button? = null

    // 拍照回传码
    val CAMERA_REQUEST_CODE = 0;
    // 相册选择回传码
    val GALLERY_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        textView = getView()?.findViewById<TextView?>(R.id.username_text)
        textView?.setText(webSocketClient.username)
        imageView = getView()?.findViewById<ImageView>(R.id.avatar_icon)
        imageView?.setImageResource(R.drawable.avatar1)
        button = getView()?.findViewById<Button>(R.id.button_modify)


        imageView?.setOnClickListener(View.OnClickListener {
            // 修改头像
            val pickIntent : Intent = Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            pickIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(pickIntent, GALLERY_REQUEST_CODE);

        })


        textView?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "修改昵称", Toast.LENGTH_LONG).show()

            // 进入修改昵称页面
            // 想法：统一写一个修改信息的fragment  activity
            // 根据修改内容的不同显示不同布局

            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, ModifyInfoActivity::class.java) }
            // intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            // intent.putExtra("username", activity?.intent?.getStringExtra("username"))
            intent.putExtra("modifyKey", "username")

            startActivity(intent)

            // activity?.finish()

        })

        button?.setOnClickListener(View.OnClickListener {
            Toast.makeText(activity, "修改密码", Toast.LENGTH_LONG).show()

            val intent = Intent()
            activity?.let { it1 -> intent.setClass(it1, ModifyInfoActivity::class.java) }
            // intent.putExtra("userId", activity?.intent?.getStringExtra("userId"))
            // intent.putExtra("username", activity?.intent?.getStringExtra("username"))
            intent.putExtra("modifyKey", "password")

            startActivity(intent)

            // activity?.finish()
        })
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_settings, container, false)
    }


    //当拍摄照片完成时会回调到onActivityResult 在这里处理照片的裁剪

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == CAMERA_REQUEST_CODE) {
//
//            }
//            else if (requestCode == GALLERY_REQUEST_CODE)
//                try {
//                    //该uri是上一个Activity返回的
//                    val imageUri = data?.getData();
//                    if(imageUri!=null) {
//                        val bit: Bitmap = BitmapFactory.decodeStream(activity?.getContentResolver()?.openInputStream(imageUri));
//
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                }
//            }
//        }
//        super.onActivityResult(requestCode, resultCode, data);
    }



    companion object {
        fun newInstance(): SettingsFragment? {
            val fragment = SettingsFragment()
            return fragment
        }
    }
}