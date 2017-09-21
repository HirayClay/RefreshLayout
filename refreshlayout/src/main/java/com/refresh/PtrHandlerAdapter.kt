package com.refresh

import android.view.View

/**
 * Created by CJJ on 2017/9/14.
 *@author CJJ
 */
class PtrHandlerAdapter : PtrHandler {
    override fun getView(): View = null!!

    override fun onPositionChange(ratio: Float) {
        /*empty*/
    }

    override fun onIdle(refreshView: RefreshLayout) {
        /*empty*/
    }

    override fun onPrepare(refreshView: RefreshLayout) {
        /*empty*/
    }

    override fun onLoading(refreshView: RefreshLayout) {
        /*empty*/
    }
}