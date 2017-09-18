package com.refresh

import android.view.View

/**
 * Created by CJJ on 2017/9/14.
 *@author CJJ
 */
interface PtrHandler {

    /**
     * the ptr switch to "Idle" state,do your "Idle" logic  here
     */
    fun onIdle(refreshView: RefreshLayout, ptr: View)

    /**
     * the ptr switch to  "Tense" and if you raise your finger ,ptr will be  loading,
     * or say ptr satisfy the "loading" condition once your raise your finger ,it will be loading state;
     * control your ptr e.g. show "pulling to refresh" text or something
     */
    fun onPrepare(refreshView: RefreshLayout, ptr: View)


    /**
     * the ptr is ready for loading,after called this method,
     * the ptr will be loading,you can trigger your own animation or do your own loading logic here
     * only happen when finger raised(fling or onStopNestedScroll)
     */
    fun onPreLoading(refreshView: RefreshLayout, ptr: View)


    /**
     * the offset ptr was pulling over,range from 0f(ptr is fully hidden) to n( when n =1f ,ptr is fully revealed)
     *  ratio is large than 1f indicates ptr is over pulled down or pulled up
     */
    fun onOffsetChange(ratio: Float)
}