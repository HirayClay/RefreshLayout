package com.refresh

import android.view.View

/**
 * Created by CJJ on 2017/9/14.
 *@author CJJ
 */
interface PtrHandler {


    fun getView(): View

    /**
     * the ptr switch to "Idle" state,do your "Idle" logic  here
     */
    fun onIdle(refreshView: RefreshLayout)

    /**
     * the ptr switch to  "Tense" and if you raise your finger ,ptr will be loading,
     * or say ptr satisfy the "refresh" condition once your raise your finger ,it will switch to refresh state;
     * control your ptr e.g. show "release to refresh" text or something
     */
    fun onPrepare(refreshView: RefreshLayout)


    /**
     * the ptr is switch to  refresh state,you can trigger your own animation or do your own refresh logic here
     */
    fun onLoading(refreshView: RefreshLayout)


    /**refreshLayout still will invoke it to remind that the refresh is complete and
     * will settle to top
     */
    fun onRefreshComplete(refreshView: RefreshLayout) {

    }

    fun onLoadMoreComplete(refreshView: RefreshLayout) {

    }

    /**
     * the offset ptr was pulling over,range from 0f(ptr is fully hidden) to n( when n =1f ,ptr is fully revealed)
     *  ratio is large than 1f indicates ptr is over pulled down or pulled up
     */
    fun onPositionChange(percent: Float)
}