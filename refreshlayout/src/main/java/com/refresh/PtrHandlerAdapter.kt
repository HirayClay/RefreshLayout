package com.refresh

import android.view.View

/**
 * Created by CJJ on 2017/9/14.
 *@author CJJ
 */
class PtrHandlerAdapter : PtrHandler {
    override fun onOffsetChange(ratio: Float) {
        /*empty*/
    }

    override fun onIdle(refreshView: RefreshLayout, ptr: View) {
        /*empty*/
    }

    override fun onPrepare(refreshView: RefreshLayout, ptr: View) {
        /*empty*/
    }

    override fun onPreLoading(refreshView: RefreshLayout, ptr: View) {
        /*empty*/
    }
}