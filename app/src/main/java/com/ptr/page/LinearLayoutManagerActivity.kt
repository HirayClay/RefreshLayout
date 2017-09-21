package com.ptr.page

import android.graphics.Rect
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.ptr.ItemsAdapter
import com.ptr.R
import com.refresh.RefreshListener
import kotlinx.android.synthetic.main.activity_linearlayoutmanager.*

class LinearLayoutManagerActivity : AppCompatActivity() {
    var ITEMS: ArrayList<String> = ArrayList()


    lateinit var itemsAdapter: ItemsAdapter
    var index: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linearlayoutmanager)
        for (i in 0..25) {
            ITEMS.add(i.toString())
        }
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
//
//            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
//                outRect.bottom = 2
//            }
//
//        })
        itemsAdapter = ItemsAdapter(ITEMS, object : ItemsAdapter.OnItemClickListener {
            override fun handle(position: Int) {

            }

        })
        recyclerView.adapter = itemsAdapter

        refreshlayout.setRefreshListener(object : RefreshListener {
            override fun onLoadMore() {
                window.decorView.postDelayed({
                    itemsAdapter.addFooter(R.mipmap.load_more, "CryPretty")
                    refreshlayout.onRefreshComplete()
                }, 1000)
            }

            override fun onRefresh() {
                window.decorView.postDelayed({
                    itemsAdapter.addHeader(R.mipmap.newly_add, "Doraemon No.${index++}")
                    refreshlayout.onRefreshComplete()
                }, 6000)
            }

        })
    }
}
