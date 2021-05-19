package com.example.pangchat

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pangchat.R
import com.example.pangchat.discover.Discover
import com.example.pangchat.discover.DiscoverAdapter
import java.util.*

/**
 * A simple [Fragment] subclass.
 * Use the [DiscoverFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DiscoverFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // TODO
        recyclerView = view.findViewById(R.id.discover_recyclerview)
        val imgs = ArrayList<Int?>()
        imgs.add(R.drawable.image1)
        imgs.add(R.drawable.image2)
        imgs.add(R.drawable.image3)
        imgs.add(R.drawable.image4)
        imgs.add(R.drawable.image5)
        imgs.add(R.drawable.image6)
        imgs.add(R.drawable.image7)
        imgs.add(R.drawable.image8)
        imgs.add(R.drawable.image9)
        imgs.add(R.drawable.image10)
        imgs.add(R.drawable.image11)
        val discovers = LinkedList<Discover?>()
        discovers.add(Discover(getString(R.string.nickname1),
                R.drawable.avatar1,
                getString(R.string.paragraph1),
                "30 分钟前",
                ArrayList()))
        discovers.add(Discover(getString(R.string.nickname2),
                R.drawable.avatar2,
                getString(R.string.paragraph2),
                "1 小时前",
                ArrayList(imgs.subList(0, 1))))
        discovers.add(Discover(getString(R.string.nickname3),
                R.drawable.avatar3,
                getString(R.string.paragraph3),
                "2 小时前",
                ArrayList(imgs.subList(1, 3))))
        discovers.add(Discover(getString(R.string.nickname4),
                R.drawable.avatar4,
                getString(R.string.paragraph4),
                "3 小时前",
                ArrayList(imgs.subList(3, 6))))
        discovers.add(Discover(getString(R.string.nickname5),
                R.drawable.avatar5,
                getString(R.string.paragraph5),
                "4 小时前",
                ArrayList(imgs.subList(6, 10))))
        discovers.add(Discover(getString(R.string.nickname6),
                R.drawable.avatar6,
                getString(R.string.paragraph6),
                "5 小时前",
                ArrayList()))
        discovers.add(Discover(getString(R.string.nickname7),
                R.drawable.avatar7,
                getString(R.string.paragraph7),
                "6 小时前",
                ArrayList(imgs.subList(10, 11))))
        val rv = recyclerView
        if (rv != null) {
            rv.adapter = DiscoverAdapter(discovers)
            rv.layoutManager = LinearLayoutManager(this.activity)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater?.inflate(R.layout.fragment_discover, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @return A new instance of fragment ExploreFragment.
         */
        fun newInstance(): DiscoverFragment? {
            return DiscoverFragment()
        }
    }
}