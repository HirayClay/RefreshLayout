package com.ptr

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.refresh.RefreshListener
import kotlinx.android.synthetic.main.activity_text_view_refresh.*

class TextViewRefreshActivity : AppCompatActivity() {

    var alternative: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_view_refresh)


        //替换自己的刷新头部


        refreshlayout.setRefreshListener(object : RefreshListener {
            override fun onLoadMore() {
                alternative = !alternative
                window.decorView.postDelayed({
                    textview.append(getString(R.string.part_text))
                    refreshlayout.onRefreshComplete()
                }, 1200)

            }

            override fun onRefresh() {
                alternative = !alternative
                window.decorView.postDelayed({
                    textview.text = getString(if (alternative) R.string.lorem else R.string.large_text)
                    refreshlayout.onRefreshComplete()
                }, 2000)
            }

        })
    }
}
