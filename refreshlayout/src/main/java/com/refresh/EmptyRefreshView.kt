package com.refresh

import android.view.View

/**
 * Created by CJJ on 2017/9/19.
 *@author CJJ
 */
class EmptyRefreshView : PtrHandler {

    var mView:View

    constructor(view: View) {
        this.mView = view
    }


    override fun getView(): View {
        return mView
    }

    override fun onIdle(refreshView: RefreshLayout) {
        /*no op*/
    }

    override fun onPrepare(refreshView: RefreshLayout) {
        /*no op*/
    }

    override fun onLoading(refreshView: RefreshLayout) {
        /*no op*/
    }

    override fun onPositionChange(ratio: Float) {
        /*no op*/
    }
}