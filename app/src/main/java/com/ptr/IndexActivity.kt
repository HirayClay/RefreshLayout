package com.ptr

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.ptr.page.CustomHeaderActivity
import com.ptr.page.GridLayoutManagerActivity
import com.ptr.page.LinearLayoutManagerActivity
import com.ptr.page.TextViewRefreshActivity
import kotlinx.android.synthetic.main.activity_index.*

class IndexActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_index)


        index_textview.setOnClickListener {
            startActivity(Intent(this@IndexActivity, TextViewRefreshActivity::class.java))
        }

        index_linearlayoutmanager.setOnClickListener {
            startActivity(Intent(this@IndexActivity, LinearLayoutManagerActivity::class.java))
        }

        index_gridlayoutmanager.setOnClickListener {
            startActivity(Intent(this@IndexActivity, GridLayoutManagerActivity::class.java))
        }
        index_ele_style_header.setOnClickListener {
            startActivity(Intent(this@IndexActivity, CustomHeaderActivity::class.java))
        }
    }
}
