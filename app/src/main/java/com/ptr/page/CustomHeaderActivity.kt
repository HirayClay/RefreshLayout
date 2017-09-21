package com.ptr.page

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.ptr.R
import com.ptr.custom.CustomCircleHeader
import com.ptr.custom.CustomRefreshHeader
import com.ptr.custom.ElmRefreshHeader
import com.refresh.RefreshHeader
import com.refresh.RefreshListener
import kotlinx.android.synthetic.main.activity_custom_header.*

class CustomHeaderActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom_header)

        refreshlayout.setHeader(ElmRefreshHeader(this))

        refreshlayout.setRefreshListener(object : RefreshListener {
            override fun onRefresh() {
                window.decorView.postDelayed({
                    refreshlayout.onRefreshComplete()
                }, 1200)
            }

            override fun onLoadMore() {
                window.decorView.postDelayed({
                    refreshlayout.onRefreshComplete()
                }, 1200)
            }

        })
    }
}
