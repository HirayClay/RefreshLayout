package com.ptr.page

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.ptr.TextImageItemAdapter
import com.ptr.R
import com.refresh.RefreshListener
import kotlinx.android.synthetic.main.activity_linearlayoutmanager.*

class LinearLayoutManagerActivity : AppCompatActivity() {


    var IMG = arrayListOf(R.mipmap.china, R.mipmap.iceland, R.mipmap.poland, R.mipmap.denmark,
            R.mipmap.german, R.mipmap.russia, R.mipmap.french, R.mipmap.finland,
            R.mipmap.czech, R.mipmap.norway, R.mipmap.swedish, R.mipmap.indian, R.mipmap.japan, R.mipmap.vietnam)
    var NAM = arrayListOf("China", "IceLand", "Poland", "Denmark", "German", "Russia", "France", "Finland", "Czech", "Norway", "Swedish"
            , "Indian", "Japan", "Vietnam")

    lateinit var itemsAdapter: TextImageItemAdapter
    var index: Int = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_linearlayoutmanager)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsAdapter = TextImageItemAdapter(IMG, NAM, R.layout.item_text, object : TextImageItemAdapter.OnItemClickListener {

            override fun onItemClick(position: Int) {
                Toast.makeText(applicationContext, "Welcome to ${NAM[position]},have fun!",Toast.LENGTH_SHORT).show()
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
                }, 1200)
            }

        })
    }
}
