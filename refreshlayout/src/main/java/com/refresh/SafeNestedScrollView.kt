package com.refresh

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent

/**
 * Created by CJJ on 2017/9/20.
 *@author CJJ
 * 之所以重写NestedScrollView,是因为在View的dispatchTouchEvent阶段，判断手指按下时候会调用stopNestedScroll(android-25 View源码的10008行)
 * ,而NestedScrollView覆写了这个stopNestedScroll方法，其逻辑会调用NestedScrollingParent（也就是RefreshLayout）的stopNestedScroll方法，
 * 导致的bug：明明是loading阶段，如果手指按下去，调用RefreshLayout的settle方法，直接settle到顶部，当时手指滑动一下马上会弹回来
 *
 * 所以我们要做的就是只有手指松开的时候才去调用NestedScrollingParent的stopNestedScroll
 */
class SafeNestedScrollView : NestedScrollView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun stopNestedScroll() {
        if (!touch)
            super.stopNestedScroll()
    }


    private var touch = false
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> touch = true
            MotionEvent.ACTION_UP -> touch = false
        }
        return super.dispatchTouchEvent(ev)
    }
}