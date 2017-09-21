package com.ptr.page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.ptr.R
import com.ptr.TextImageItemAdapter
import com.ptr.custom.ElmRefreshHeader
import com.refresh.RefreshListener
import kotlinx.android.synthetic.main.activity_grid_layout_manager.*
import java.util.*

class GridLayoutManagerActivity : AppCompatActivity() {

    lateinit var itemAdapter: TextImageItemAdapter

    val IMG = arrayListOf(
            R.mipmap.f1, R.mipmap.f2, R.mipmap.f3, R.mipmap.f4, R.mipmap.f5, R.mipmap.f6,
            R.mipmap.f7, R.mipmap.f8, R.mipmap.f9, R.mipmap.f10, R.mipmap.f11, R.mipmap.f12,
            R.mipmap.f13, R.mipmap.f14, R.mipmap.f15, R.mipmap.f16, R.mipmap.f17, R.mipmap.f18,
            R.mipmap.f19, R.mipmap.f20, R.mipmap.f21, R.mipmap.f22, R.mipmap.f23, R.mipmap.f24,
            R.mipmap.f25, R.mipmap.f26, R.mipmap.f27, R.mipmap.f28)

    val NAM = arrayListOf(
            "beverage", "tea", "coffe", "chicken leg", "poor chicken", "bacon",
            "fish soup", "noodle", "cola", "wine", "cheer", "orange juice",
            "ice cream", "ice cream", "oreo", "loaf", "sandwich", "cookie",
            "apple", "grapes", "mango", "orange", "strawberry", "banana"
            , "cherry", "pear", "xigua", "beer")

    val vowel = arrayListOf('a', 'e', 'i', 'o', 'u')
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_layout_manager)

        itemAdapter = TextImageItemAdapter(IMG, NAM, R.layout.grid_item, object : TextImageItemAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {
                val c = NAM[position].first()
                Toast.makeText(applicationContext,
                        "you click ${if (vowel.contains(c)) "an" else "a"}  ${NAM[position]}",
                        Toast.LENGTH_SHORT).show()
            }


        })

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = itemAdapter

        refreshlayout.setHeader(ElmRefreshHeader(this))
        refreshlayout.setRefreshListener(object : RefreshListener {
            override fun onRefresh() {
                window.decorView.postDelayed({
                    IMG.reverse()
                    NAM.reverse()
                    itemAdapter.notifyDataSetChanged()
                    refreshlayout.onRefreshComplete()
                }, 1200)
            }

            override fun onLoadMore() {
                window.decorView.postDelayed({
                    val r = Random().nextInt(IMG.size)
                    itemAdapter.addFooter(IMG[r], NAM[r])
                    refreshlayout.onLoadMoreComplete()

                }, 1200)
            }
        })
    }
}
